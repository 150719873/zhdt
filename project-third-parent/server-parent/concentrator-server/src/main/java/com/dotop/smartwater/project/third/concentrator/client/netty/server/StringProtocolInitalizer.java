package com.dotop.smartwater.project.third.concentrator.client.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-03 17:55
 **/
@Component
@Qualifier("springProtocolInitializer")
public class StringProtocolInitalizer extends ChannelInitializer<SocketChannel> {
    @Autowired
    MyDecode stringDecoder;

    @Autowired
    StringEncoder stringEncoder;

    @Autowired
    ServerHandler serverHandler;

    //1800秒读不到客户端心跳，该连接视为无效连接,服务器断开
    //读操作空闲300秒
    private final static int readerIdleTimeSeconds = 1800;
    //写操作空闲240秒
    private final static int writerIdleTimeSeconds = 240;
    //读写全部空闲180秒
    private final static int allIdleTimeSeconds = 180;

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("ping", new IdleStateHandler(readerIdleTimeSeconds, writerIdleTimeSeconds, allIdleTimeSeconds,TimeUnit.SECONDS));
        pipeline.addLast("decoder", stringDecoder);
        pipeline.addLast("encoder", stringEncoder);
        pipeline.addLast("handler", serverHandler);
    }

}
