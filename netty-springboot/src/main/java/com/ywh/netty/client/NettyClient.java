package com.ywh.netty.client;

import com.ywh.netty.protobuf.UserMsg;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * @author ywh
 */
@Service
@Slf4j
public class NettyClient {


    @Value("${server.bind_address}")
    private String host;

    @Value("${server.bind_port}")
    private Integer port;

    /**
     * 唯一标记
     */
    private boolean initFlag = true;

    private EventLoopGroup group;

    private ChannelFuture f;

    /**
     * Netty 创建全部都是实现自 AbstractBootstrap。 客户端的是 Bootstrap，服务端的则是 ServerBootstrap。
     **/
    @PostConstruct
    public void init() {
        group = new NioEventLoopGroup();
        doConnect(new Bootstrap(), group);
    }

    @PreDestroy
    public void shutdown() throws InterruptedException {
        log.info("正在停止客户端");
        try {
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
        log.info("客户端已停止!");
    }

    /**
     * 重连
     */
    void doConnect(Bootstrap bootstrap, EventLoopGroup group) {
        try {
            if (bootstrap != null) {
                bootstrap.group(group);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.handler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ChannelPipeline ph = ch.pipeline();
                        // 解码和编码应和服务端一致

                        // 入参说明: 读超时时间、写超时时间、所有类型的超时时间、时间格式，心跳的读写时间应该小于服务端所设置的时间
                        ph.addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));

                        // 传输的协议 Protobuf
                        ph.addLast(new ProtobufVarint32FrameDecoder());
                        ph.addLast(new ProtobufDecoder(UserMsg.User.getDefaultInstance()));
                        ph.addLast(new ProtobufVarint32LengthFieldPrepender());
                        ph.addLast(new ProtobufEncoder());

                        // 业务逻辑实现类
                        ph.addLast("nettyClientHandler", new NettyClientHandler());
                    }
                });
                bootstrap.remoteAddress(host, port);
                f = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
                    final EventLoop eventLoop = futureListener.channel().eventLoop();
                    if (!futureListener.isSuccess()) {
                        log.info("与服务端断开连接！在 10s 之后尝试重连！");
                        eventLoop.schedule(() -> doConnect(new Bootstrap(), eventLoop), 10, TimeUnit.SECONDS);
                    }
                });
                if (initFlag) {
                    log.info("Netty 客户端启动成功！");
                    initFlag = false;
                }
            }
        } catch (Exception e) {
            log.info("客户端连接失败！" + e.getMessage());
        }
    }
}
