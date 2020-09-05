package com.dotop.smartwater.project.third.server.meter.netty.server;

import com.dotop.smartwater.project.third.server.meter.netty.decoder.CombineDecoder;
import com.dotop.smartwater.project.third.server.meter.netty.decoder.CommonDecoder;
import com.dotop.smartwater.dependence.core.log.LogMsg;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 *
 * https://www.jianshu.com/p/ac8b7549698a
 */
public class ProtocolInitalizer extends ChannelInitializer<SocketChannel> {

    private static final Logger logger = LogManager.getLogger(ProtocolInitalizer.class);

    @Autowired
    private CombineDecoder combineDecoder;

    @Autowired
    private CommonDecoder commonDecoder;

    @Autowired
    private StringEncoder stringEncoder;

    @Autowired
    private ServerHandler serverHandler;

    // 1800秒读不到客户端心跳，该连接视为无效连接,服务器断开
    // 读操作空闲300秒
    private final static int READER_IDLE_TIME_SECONDS = 300;
    // 写操作空闲240秒
    private final static int WRITER_IDLE_TIME_SECONDS = 240;
    // 读写全部空闲180秒
    private final static int ALL_IDLE_TIME_SECONDS = 180;

    @Override
    protected void initChannel(SocketChannel ch) {
        try {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast("ping", new IdleStateHandler(READER_IDLE_TIME_SECONDS, WRITER_IDLE_TIME_SECONDS, ALL_IDLE_TIME_SECONDS, TimeUnit.SECONDS));
            pipeline.addLast("countHandler", new CountHandler());
            // 编码
            pipeline.addLast("stringEncoder", stringEncoder);
            // 解码
            pipeline.addLast("commonDecoder", commonDecoder);
            pipeline.addLast("combineDecoder", combineDecoder);
            // 处理
            pipeline.addLast("serverHandler", serverHandler);
        } catch (Exception e) {
            logger.error(LogMsg.to("initChannel发生错误", e));
        }
    }


    @ChannelHandler.Sharable
    class CountHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            try {
                logger.info("Remote Address : " + ctx.channel().remoteAddress() + " active !");
                InetSocketAddress inteSocket = (InetSocketAddress) ctx.channel().remoteAddress();
                logger.info("端口 已有客户端连接***" + inteSocket.getPort());
                super.channelActive(ctx);
            } catch (Exception e) {
                logger.error(LogMsg.to("channelActive发生错误", e));
            }
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            super.channelRead(ctx, msg);
            logger.info("接受到.." + msg);
        }
    }

}
