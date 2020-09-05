package com.dotop.smartwater.project.third.concentrator.client.netty.business;

import com.dotop.smartwater.project.third.concentrator.core.bo.TerminalMeterReadBo;
import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.cache.api.AbstractListCache;
import com.dotop.smartwater.project.third.concentrator.api.ITerminalCallbackFactory;
import com.dotop.smartwater.project.third.concentrator.client.netty.utils.CtxUtils;
import com.dotop.smartwater.project.third.concentrator.client.netty.utils.ToolUtils;
import com.dotop.smartwater.project.third.concentrator.core.constants.CacheKey;
import com.dotop.smartwater.project.third.concentrator.core.constants.ConcentratorConstants;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @program: project-parent
 * @description: 处理socket业务
 *
 * @create: 2019-06-21 11:20
 **/
@Service
public class HandleMsg {

    private static final Logger log = LoggerFactory.getLogger(HandleMsg.class);

    @Autowired
    private StringValueCache redisDao;

    @Autowired
    private AbstractListCache abstractListCache;

    @Autowired
    private CtxUtils ctxUtils;

    @Autowired
    private ITerminalCallbackFactory iTerminalCallbackFactory;

    public void handle(ChannelHandlerContext ctx, String msg) {
        //读取集中器标识
        String ctxName = msg.substring(14, 24);
        String num = ToolUtils.concentratorEncode(ctxName);
        log.info("集中器地址域：{}, 编号为：{}", ctxName, num);

        String flag = msg.substring(28, 36);

        //根据单元标识判断要处理的业务
        switch (flag) {
            //心跳
            case "10000400":
                ctxUtils.heartBeat(ctxName, ctx);
                iTerminalCallbackFactory.heartbeat(null, ConcentratorConstants.TASK_TYPE_HEARTBEAT, num, new Date(),
                        msg.substring(36, 38));

                noticeDataUploadComplete(ctxName);
                break;
            default:
                iTerminalCallbackFactory.heartbeat(null, null, num, new Date(),
                        msg.substring(36, 38));
                switch (flag) {
                    //读取上报时间
                    case "01012003":
                        redisDao.set(ctxName, msg, 2L);
                        break;
                    //设置上报时间/读档案/读时钟
                    case "00000100":
                        redisDao.set(ctxName, msg, 2L);
                        break;
                    //下载档案失败/读档失败/开阀失败/关阀失败
                    case "00000200":
                        redisDao.set(ctxName, msg, 5L);
                        break;
                    //获取是否允许上报
                    case "01014003":
                        redisDao.set(ctxName, msg, 2L);
                        break;
                    //读GPRS/设置GPRS
                    case "00004000":
                        redisDao.set(ctxName, msg, 2L);
                        break;
                    //读表
                    case "00000107":
                        readMeter(ctxName, msg, num);
                        break;
                    //登录
                    case "10000100":
                        if (ctxUtils.login(ctxName, ctx)) {
                            redisDao.set(ctxName, CtxUtils.CONNECTION_CHANGE, 2L);
                        }
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    private void readMeter(String ctxName, String msg, String num) {
        //根据SEQ区分是定时上报的数据还是主动下发的数据（主动下发需要确认，自动上报的不需要回复）
        String seq = msg.substring(26, 28);
        if ("60".equals(seq)) {
            //通知http那边阻塞
            iTerminalCallbackFactory.block(null, null, ConcentratorConstants.TASK_TYPE_AUTO_UPLOAD, num, true);

            //解析自动上报的数据
            Integer count = Integer.parseInt(msg.substring(38, 40) + msg.substring(36, 38), 16);

            List<TerminalMeterReadBo> terminalMeterReadBos = new ArrayList<>();
            Date date = new Date();
            int index;
            for (int i = 0; i < count; i++) {
                index = 40;
                index = index + i * 7 * 2;
                TerminalMeterReadBo tmr = new TerminalMeterReadBo();
                tmr.setNo(String.valueOf(Integer.parseInt(msg.substring(index + 2, index + 4)
                        + msg.substring(index, index + 2), 16)));
                tmr.setConcentratorCode(num);

                /**
                 * 是否带阀(water常量)；1，带阀；0，不带阀
                 */
                tmr.setValve(null);
                /*tmr.setValveStatus(msg.substring(index + 12, index + 14).equals("01") ?
                        ConcentratorConstants.VALVE_STATUS_OPEN : ConcentratorConstants.VALVE_STATUS_CLOSE);*/

                // TODO 阀门状态
                tmr.setValveStatus(null);

                tmr.setReceiveDate(date);

                String water = msg.substring(index + 4, index + 12);
                ToolUtils.makeTerminalMeterRead(tmr, water);
                terminalMeterReadBos.add(tmr);
            }

            abstractListCache.rightPush(CacheKey.UPLOAD_DATA_CACHE + ctxName, terminalMeterReadBos);

        } else {
            redisDao.set(ctxName, msg, 10L);
        }
    }

    private void noticeDataUploadComplete(String ctxName) {
        List<TerminalMeterReadBo> data = abstractListCache.lrange(CacheKey.UPLOAD_DATA_CACHE + ctxName,
                TerminalMeterReadBo.class, 0, -1);
        if (data == null || data.size() == 0) {
            return;
        }
        try {
            //当收到心跳，说明已经上报完所有读数，调嘉毅接口,通知批量更新读数
            iTerminalCallbackFactory.meterReads(null, null, ConcentratorConstants.TASK_TYPE_AUTO_UPLOAD,
                    data);
            redisDao.del(CacheKey.UPLOAD_DATA_CACHE + ctxName);
            log.info("{} 删除redis里面的上报数据成功", ctxName);
        } catch (Exception e) {
            log.info("{} 回调 【iTerminalCallbackFactory.meterReads】接口报错: {}", ctxName, e.getMessage());
        }
    }
}
