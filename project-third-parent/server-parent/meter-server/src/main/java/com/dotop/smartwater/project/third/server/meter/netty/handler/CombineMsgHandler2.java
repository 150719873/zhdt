package com.dotop.smartwater.project.third.server.meter.netty.handler;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.dependence.core.exception.BaseExceptionConstants;
import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.dependence.core.utils.BeanUtils;
import com.dotop.smartwater.dependence.core.utils.DateUtils;
import com.dotop.smartwater.dependence.core.utils.JSONObjects;
import com.dotop.smartwater.dependence.core.utils.JSONUtils;
import com.dotop.smartwater.project.third.module.api.factory.IMeterDockingFactory;
import com.dotop.smartwater.project.third.module.api.factory.kbl.IKblDeviceFactory;
import com.dotop.smartwater.project.third.module.core.constants.DockingConstants;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceForm;
import com.dotop.smartwater.project.third.module.core.water.form.DeviceUplinkForm;
import com.dotop.smartwater.project.third.module.core.water.form.DockingForm;
import com.dotop.smartwater.project.third.module.core.water.vo.DockingVo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
@Deprecated
public class CombineMsgHandler2 {

    private static final Logger logger = LogManager.getLogger(CombineMsgHandler2.class);

    /**
     * 一帧数据的起始符
     */
    private static final String PREFIX = "~";
    /**
     * 帧终止符
     */
    private static final String SUFFIX = "@";
    /**
     * 分隔符
     */
    private static final String DIVISION = "\\|";
    /**
     * 采样起始符
     */
    private static final String START = "\\*";
    /**
     * 水表号位置
     */
    private static final int DEVNO = 1;
    private static final int UPLINK_DATE = 1;
    private static final int WATER = 2;

    @Autowired
    private StringValueCache svc;
    @Autowired
    private IMeterDockingFactory iMeterDockingFactory;
    @Autowired
    private IKblDeviceFactory iKblDeviceFactory;

    public void handle(ChannelHandlerContext ctx, String msg) {
        logger.info("msg:{}", msg);
        try {
            logger.info("开始校验数据----------------------");
            JSONObjects jsonObjects = JSONUtils.parseObject(msg);
            String result = jsonObjects.getString("data");
            if (regular(result)) {
                logger.info("正则校验通过----------------------");
            } else {
                throw new FrameworkRuntimeException(BaseExceptionConstants.PARSE_EXCEPTION, "正则校失败----------------------");
            }
            DeviceForm deviceForm = analyseFlow(result);
            DockingForm dockingForm = new DockingForm();
            dockingForm.setType(DockingConstants.KBL_NB_USER_INFO);
            DockingVo dockingVo = iMeterDockingFactory.get(dockingForm);
            //TODO 需要校验登录信息
            iKblDeviceFactory.updateDevice(BeanUtils.copy(dockingVo, DockingForm.class), deviceForm);
            logger.info(LogMsg.to("deviceForm", deviceForm));
            logger.info("更新抄表数据成功------------");
        } catch (Exception e) {
            logger.error(LogMsg.to(e.getMessage(), e));
        }
    }

    private static boolean regular(String str) {
        try {
            if (StringUtils.isNotBlank(str)) {
                String pattern = "^~\\|[A-Za-z0-9.]+(\\*\\|\\d{4}-\\d{1,2}-\\d{1,2}\\s+\\d{2}:\\d{2}:\\d{2}\\|((\\-|\\+)?\\d+\\.?\\d+\\|){3})+@$";
                Pattern r = Pattern.compile(pattern);
                Matcher m = r.matcher(str);
                return m.find();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析无线流量数据协议
     *
     * @param str
     * @return
     * @throws FrameworkRuntimeException
     */
    private DeviceForm analyseFlow(String str) throws FrameworkRuntimeException {
        DeviceForm deviceForm = new DeviceForm();
        List<DeviceUplinkForm> deviceUplinkForms = new ArrayList<>();
        try {
            if (str.startsWith(PREFIX) && str.endsWith(SUFFIX)) {
                //去掉前后缀
                str = str.replaceAll(PREFIX, "").replaceAll(SUFFIX, "");
                //以采样起始符切割
                String[] splits = str.split(START);
                for (int i = 0; i < splits.length; i++) {
                    //以采样分隔符切割
                    String[] values = splits[i].split(DIVISION);
                    if (i == 0) {
                        deviceForm.setDevno(values[DEVNO]);
                    } else {
                        DeviceUplinkForm deviceUplinkForm = new DeviceUplinkForm();
                        deviceUplinkForm.setDevno(deviceForm.getDevno());
                        deviceUplinkForm.setUplinkDate(DateUtils.parse(values[UPLINK_DATE], DateUtils.DATETIME));
                        deviceUplinkForm.setWater(Double.parseDouble(values[WATER]));
                        deviceUplinkForms.add(deviceUplinkForm);
                    }
                }
            }
            deviceUplinkForms.sort(Comparator.comparing(DeviceUplinkForm::getUplinkDate, Comparator.reverseOrder()));
            deviceForm.setWater(deviceUplinkForms.get(0).getWater());
            deviceForm.setDeviceUplinkForms(deviceUplinkForms);
            return deviceForm;
        } catch (Exception e) {
            throw new FrameworkRuntimeException(BaseExceptionConstants.PARSE_EXCEPTION, e, "数据解析异常");
        }
    }
}
