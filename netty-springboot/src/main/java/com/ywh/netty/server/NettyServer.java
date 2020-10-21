package com.ywh.netty.server;

import com.ywh.netty.protobuf.UserMsg;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ResourceLeakDetector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @author ywh
 */
@Service
@Slf4j
public class NettyServer {

    /**
     * 通过 Spring Boot 读取静态资源，实现 netty 配置文件的读写
     */
    @Value("${server.bind_port}")
    private Integer port;

    @Value("${server.bind_host}")
    private String host;

    @Value("${server.netty.boss_group_thread_count}")
    private Integer parentGroupThreadCount;

    @Value("${server.netty.worker_group_thread_count}")
    private Integer childGroupThreadCount;

    @Value("${server.netty.leak_detector_level}")
    private String leakDetectorLevel;

    @Value("${server.netty.max_payload_size}")
    private Integer maxPayloadSize;

    private ChannelFuture channelFuture;

    // 创建 parent 线程组用于服务端接收客户端的连接、child 线程组用于进行 SocketChannel 的数据读写。

    private EventLoopGroup parentGroup;

    private EventLoopGroup childGroup;

    // @PostConstruct
    public ChannelFuture bind() {
        InetSocketAddress address = new InetSocketAddress(host, port);
        return bind(address);
    }


    public ChannelFuture bind(InetSocketAddress address) {
        parentGroup = new NioEventLoopGroup(parentGroupThreadCount);
        childGroup = new NioEventLoopGroup(childGroupThreadCount);

        log.info("Setting resource leak detector level to {}", leakDetectorLevel);
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.valueOf(leakDetectorLevel.toUpperCase()));
        log.info("starting server...");
        // 创建 ServerBootstrap 对象。
        ServerBootstrap serverBootstrap = new ServerBootstrap()
            // 设置 EventLoopGroup。
            .group(parentGroup, childGroup)
            // 设置实例化 NioServerSocketChannel 类。
            .channel(NioServerSocketChannel.class)
            // 设置 NioServerSocketChannel 的处理器。
            .handler(new LoggingHandler(LogLevel.INFO))
            // 设置连入服务端的 Client 的 SocketChannel 的处理器。
            .childHandler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {

                        // ChannelPipeline：返回和该条连接相关的逻辑处理链，采用了责任链模式。
                        ch.pipeline()
                            // 入参说明: 读超时时间、写超时时间、所有类型的超时时间、时间格式。
                            .addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS))

                            // 解码和编码和客户端一致，传输的协议 Protobuf。
                            .addLast(new ProtobufVarint32FrameDecoder())
                            .addLast(new ProtobufDecoder(UserMsg.User.getDefaultInstance()))
                            .addLast(new ProtobufVarint32LengthFieldPrepender())
                            .addLast(new ProtobufEncoder())

                            // 业务逻辑实现类。
                            .addLast("nettyServerHandler", new NettyServerHandler());
                    }
                }
            );
        // 绑定端口，并同步等待成功，即启动服务端。
        channelFuture = serverBootstrap.bind(address).syncUninterruptibly();
        log.info("server started!");
        return channelFuture;
    }

    /**
     *
     */
    @PreDestroy
    public void destroy() {
        log.info("stopping Server...");
        Channel channel = channelFuture.channel();
        if (null == channel) {
            return;
        }
        channel.closeFuture().syncUninterruptibly();
        parentGroup.shutdownGracefully();
        childGroup.shutdownGracefully();
        log.info("server stopped!");
    }

    /**
     *
     * @return
     */
    public Channel getChannel() {
        return channelFuture.channel();
    }
}
