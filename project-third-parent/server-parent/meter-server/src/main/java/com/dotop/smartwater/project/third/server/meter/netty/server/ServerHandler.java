package com.dotop.smartwater.project.third.server.meter.netty.server;


import com.dotop.smartwater.project.third.server.meter.netty.handler.CombineMsgHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * // @Sharable
 */
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    private static final Logger logger = LogManager.getLogger(ServerHandler.class);

    @Autowired
    private CombineMsgHandler combineMsgHandler;

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String msg) {
        logger.info("收到客户端信息 :" + msg);
        combineMsgHandler.handle(ctx, msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("Remote Address : " + ctx.channel().remoteAddress() + " active !");
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.info("Throwable : " + cause);
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("[{}]Channel is disconnected", ctx.channel().remoteAddress());
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
                logger.info(ctx.channel().remoteAddress() + " 未进行读操作,判定客户端连接无效,断开");
                // 超时关闭channel
                ctx.close();
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                logger.info(ctx.channel().remoteAddress() + " WRITER_IDLE");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                // 未进行读写
                logger.info(ctx.channel().remoteAddress() + " ALL_IDLE");
            }
        }
    }
}
