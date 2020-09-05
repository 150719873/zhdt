package com.dotop.smartwater.project.third.server.meter.netty.config;

import com.dotop.smartwater.project.third.server.meter.netty.decoder.CombineDecoder;
import com.dotop.smartwater.project.third.server.meter.netty.decoder.CommonDecoder;
import com.dotop.smartwater.project.third.server.meter.netty.encoder.CombineEncoder;
import com.dotop.smartwater.project.third.server.meter.netty.handler.CombineMsgHandler;
import com.dotop.smartwater.project.third.server.meter.netty.server.ProtocolInitalizer;
import com.dotop.smartwater.project.third.server.meter.netty.server.ServerHandler;
import com.dotop.smartwater.project.third.server.meter.netty.server.TcpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Configuration
public class NettyConfig {

    @Value("${netty.boss.thread.count:16}")
    private int bossCount;

    @Value("${netty.worker.thread.count:16}")
    private int workerCount;

    @Value("${netty.tcp.port:34567}")
    private int tcpPort;

    @Value("${netty.socket.keepalive:true}")
    private boolean keepAlive;

    @Value("${netty.socket.backlog:1024}")
    private int backlog;

    @Bean("protocolInitalizer")
    public ChannelInitializer protocolInitalizer() {
        return new ProtocolInitalizer();
    }

    @Bean("serverHandler")
    public ServerHandler serverHandler() {
        return new ServerHandler();
    }

    @Bean
    public CombineMsgHandler combineMsgHandler() {
        return new CombineMsgHandler();
    }

    @Bean
    public TcpServer tcpServer() {
        return new TcpServer();
    }

    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup(), workerGroup())
                // 设置TCP缓冲区
                .option(ChannelOption.SO_BACKLOG, backlog)
                // 不延迟，消息立即发送
                .childOption(ChannelOption.TCP_NODELAY, true)
                // 长连接
                .childOption(ChannelOption.SO_KEEPALIVE, keepAlive)
                .channel(NioServerSocketChannel.class)
                .childHandler(protocolInitalizer());
        return bootstrap;
    }

    /**
     * 用于接受客户端连接的线程工作组
     */
    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    /**
     * 用于对接受客户端连接读写操作的线程工作组
     */
    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }

    @Bean(name = "tcpSocketAddress")
    public InetSocketAddress tcpPort() {
        return new InetSocketAddress(tcpPort);
    }

    @Bean(name = "tcpChannelOptions")
    public Map<ChannelOption<?>, Object> tcpChannelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<>(10);
        options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
        options.put(ChannelOption.SO_BACKLOG, backlog);
        return options;
    }

    @Bean(name = "combineEncoder")
    public CombineEncoder combineEncoder() {
        return new CombineEncoder(CharsetUtil.UTF_8);
    }

    @Bean(name = "stringEncoder")
    public StringEncoder stringEncoder() {
        return new StringEncoder(CharsetUtil.UTF_8);
    }

    @Bean(name = "combineDecoder")
    public CombineDecoder combineDecoder() {
        return new CombineDecoder();
    }

    @Bean(name = "commonDecoder")
    public CommonDecoder commonDecoder() {
        return new CommonDecoder(CharsetUtil.UTF_8);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}
