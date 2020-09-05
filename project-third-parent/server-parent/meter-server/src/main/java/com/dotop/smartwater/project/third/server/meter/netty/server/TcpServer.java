package com.dotop.smartwater.project.third.server.meter.netty.server;

import com.dotop.smartwater.dependence.core.log.LogMsg;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TcpServer {

    private static final Logger logger = LogManager.getLogger(TcpServer.class);

    @Resource(name = "serverBootstrap")
    private ServerBootstrap bootstrap;

    @Resource(name = "tcpSocketAddress")
    private InetSocketAddress tcpPort;

    @Resource(name = "bossGroup")
    private NioEventLoopGroup bossGroup;

    @Resource(name = "workerGroup")
    private NioEventLoopGroup workerGroup;

    private List<ChannelFuture> serverChannelFutures = new ArrayList<>();

    @PostConstruct
    public void start() throws Exception {
        try {
            logger.info("Starting socket server at " + tcpPort);
            ChannelFuture channelFuture = bootstrap.bind(tcpPort).sync();
            serverChannelFutures.add(channelFuture);
            // TODO
            channelFuture = bootstrap.bind(new InetSocketAddress(34568)).sync();
            serverChannelFutures.add(channelFuture);
        } catch (Exception e) {
            logger.error(LogMsg.to("启动发生错误", e));
        }
    }

    @PreDestroy
    public void stop() throws Exception {
        try {
            logger.info("Stop socket server at " + tcpPort);
            for (ChannelFuture channelFuture : serverChannelFutures) {
                channelFuture.channel().closeFuture().sync();
            }
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        } catch (Exception e) {
            logger.error(LogMsg.to("停止发生错误", e));
        }
    }

}
