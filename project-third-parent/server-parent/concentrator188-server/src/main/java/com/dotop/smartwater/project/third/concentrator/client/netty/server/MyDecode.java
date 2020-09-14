package com.dotop.smartwater.project.third.concentrator.client.netty.server;

import com.dotop.smartwater.dependence.cache.StringValueCache;
import com.dotop.smartwater.project.third.concentrator.client.netty.utils.StrUtil;
import com.dotop.smartwater.project.third.concentrator.client.netty.utils.ToolUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-06 15:01
 **/
@Service
public class MyDecode extends StringDecoder {

    private static final String hexes = "0123456789ABCDEF";
    private static final Logger logger = LoggerFactory.getLogger(MyDecode.class);


    @Autowired
    private StringValueCache redisDao;


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {

        byte[] req = new byte[msg.readableBytes()];
        msg.readBytes(req);
        final StringBuilder hex = new StringBuilder(2 * req.length);
        for (byte b : req) {
            hex.append(hexes.charAt((b & 0xF0) >> 4))
                    .append(hexes.charAt((b & 0x0F)));
        }

        String dataStr = hex.toString();

        String address = String.valueOf(ctx.channel().remoteAddress());

        //解决集中器分包问题
        if (ToolUtils.verifyDataLegitimacy(dataStr)) {
            //收到完成的信息,清空以前的缓存
            redisDao.del(address);
            out.add(dataStr);
        } else {
            String lastMsg = redisDao.get(address);
            if (StrUtil.isBlank(lastMsg)) {
                logger.info("不完整的消息: {}", dataStr);
                redisDao.set(address, dataStr, 120L);
            } else {
                //第二条是否包含第一条所有内容,是的话把第二条置换为第一条
                if (dataStr.startsWith(ToolUtils.START_PARAM) && dataStr.startsWith(lastMsg)) {
                    logger.info("第二条包含第一条所有内容,进行置换: {}", dataStr);
                    redisDao.set(address, dataStr, 120L);
                    return;
                }

                //组装消息
                dataStr = lastMsg + dataStr;
                if (ToolUtils.verifyDataLegitimacy(dataStr)) {
                    out.add(dataStr);
                    logger.info("断包组装成功完整的消息: {}", dataStr);
                    redisDao.del(address);
                } else {
                    logger.info("断包组装校验不通过,继续等待下一条消息: {}", dataStr);
                    redisDao.set(address, dataStr, 120L);
                }
            }
        }
    }
}
