package com.dotop.smartwater.project.third.server.meter.netty.handler;

import com.dotop.smartwater.dependence.core.exception.FrameworkRuntimeException;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import com.dotop.smartwater.project.module.core.water.constants.ResultCode;
import com.dotop.smartwater.project.third.server.meter.core.NumberUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MsgHandler {

    private final Logger logger = LogManager.getLogger(MsgHandler.class);

    protected void sendMsg(ChannelHandlerContext ctx, String msg) {
        try {
            if (ctx != null && !ctx.isRemoved()) {
                //netty需要用ByteBuf传输,使用池化buffer，容量可随data大小改变
                byte[] data = NumberUtils.hexToBytes(msg);
                ByteBuf buff = ctx.alloc().buffer(data.length);
                //对接需要16进制
                buff.writeBytes(data);
                ChannelFuture cf = ctx.writeAndFlush(buff);
                cf.addListener((ChannelFutureListener) channelFuture -> {
                    if (channelFuture.isSuccess() && channelFuture.isDone()) {
                        logger.info(LogMsg.to("msg", "成功发送消息:" + msg));
                    }
                });
            } else {
                logger.error(LogMsg.to("msg", "连接丢失，请稍候再试！"));
                throw new FrameworkRuntimeException(ResultCode.Fail, "连接丢失，请稍候再试！");
            }
        } catch (Exception e) {
            logger.error(LogMsg.to("e", e, "msg", "回复失败"));
            throw new FrameworkRuntimeException(ResultCode.Fail, "回复失败");
        }
    }
}
