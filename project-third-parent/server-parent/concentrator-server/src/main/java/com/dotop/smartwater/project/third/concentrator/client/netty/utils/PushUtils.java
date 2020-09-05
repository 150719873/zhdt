package com.dotop.smartwater.project.third.concentrator.client.netty.utils;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.project.third.concentrator.client.netty.dc.model.code.ResultCode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-10 14:27
 **/
public class PushUtils {
    private static final Logger log = LoggerFactory.getLogger(PushUtils.class);

    public static void sendMsg(ChannelHandlerContext ctx, String msg) {
        try {
            if (ctx != null && !ctx.isRemoved()) {
                //netty需要用ByteBuf传输,使用池化buffer，容量可随data大小改变
                byte[] data = BinaryConversionUtils.hexString2Bytes(msg);
                ByteBuf buff = ctx.alloc().buffer(data.length);
                //对接需要16进制
                buff.writeBytes(data);

                ChannelFuture cf = ctx.writeAndFlush(buff);
                cf.addListener((ChannelFutureListener) channelFuture -> {
                    if (channelFuture.isSuccess() && channelFuture.isDone()) {
                        log.info("成功发送消息:" + msg);
                    }
                });
            }else {
                throw new FrameworkRuntimeException(String.valueOf(ResultCode.Fail), "集中器连接丢失，请稍候再试！");
            }

        } catch (Exception e) {
            throw new FrameworkRuntimeException(String.valueOf(ResultCode.Fail), "下发集中器失败");
        }

    }
}
