package com.dotop.smartwater.project.third.concentrator.client.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: dingtong
 * @description:
 *
 * @create: 2019-06-03 17:53
 **/
@Configuration
public class NettyConfig {

    @Value("${netty.boss.thread.count}")
    private int bossCount;

    @Value("${netty.worker.thread.count}")
    private int workerCount;

    @Value("${netty.tcp.port}")
    private int tcpPort;

    @Value("${netty.socket.keepalive}")
    private boolean keepAlive;

    @Value("${netty.socket.backlog}")
    private int backlog;

    @Autowired
    @Qualifier("springProtocolInitializer")
    private StringProtocolInitalizer protocolInitalizer;

    @SuppressWarnings("unchecked")
    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(protocolInitalizer);
        //连接数
        b.option(ChannelOption.SO_BACKLOG, backlog);

        //不延迟，消息立即发送
        b.childOption(ChannelOption.TCP_NODELAY, true);
        //长连接
        b.childOption(ChannelOption.SO_KEEPALIVE, keepAlive);

        return b;
    }

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

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

    @Bean(name = "stringEncoder")
    public StringEncoder stringEncoder() {
        return new StringEncoder(CharsetUtil.UTF_8);
    }

    @Bean(name = "stringDecoder")
    public MyDecode stringDecoder() {
        return new MyDecode();
    }

    /**
     * Necessary to make the Value annotations work.
     *
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
