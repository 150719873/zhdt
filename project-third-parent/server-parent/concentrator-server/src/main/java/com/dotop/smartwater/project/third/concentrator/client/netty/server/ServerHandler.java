package com.dotop.smartwater.project.third.concentrator.client.netty.server;


import com.dotop.smartwater.project.third.concentrator.client.netty.business.HandleMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static io.netty.channel.ChannelHandler.Sharable;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-03 17:58
 **/
@Component
@Qualifier("serverHandler")
@Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger log = LoggerFactory.getLogger(ServerHandler.class);

    @Autowired
    private HandleMsg handleMsg;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        log.info("收到客户端信息 :" + msg);
        handleMsg.handle(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("Remote Address : " + ctx.channel().remoteAddress() + " active !");
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("[{}]Channel is disconnected", ctx.channel().remoteAddress());
        ctx.close();
        super.channelInactive(ctx);
    }

    /**
     * 一段时间未进行读写操作 回调
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                // 未进行读操作,判定客户端连接无效,断开
                log.info(ctx.channel().remoteAddress() + " 未进行读操作,判定客户端连接无效,断开");
                // 超时关闭channel
                ctx.close();
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                // log.info(ctx.channel().remoteAddress() + " WRITER_IDLE");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                // 未进行读写
                // log.info(ctx.channel().remoteAddress() + " ALL_IDLE");
            }
        }
    }
}
