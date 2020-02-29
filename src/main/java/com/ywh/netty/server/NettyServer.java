package com.ywh.netty.server;

import com.ywh.netty.codec.PacketDecoder;
import com.ywh.netty.codec.PacketEncoder;
import com.ywh.netty.codec.Spliter;
import com.ywh.netty.server.handler.LoginRequestHandler;
import com.ywh.netty.server.handler.MessageRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

/**
 * 服务启动流程
 *
 * @author ywh
 * @since 24/12/2019
 */
public class NettyServer {
    public static void main(String[] args) {
        // bossGroup 用于接收请求创建连接，workerGroup 用于读取数据处理业务逻辑
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(), workerGroup = new NioEventLoopGroup();

        // 引导服务端启动工作
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
            // 配置两个线程组
            .group(bossGroup, workerGroup)

            // 指定 NIO 模型（BIO 则对应 OioServerSocketChannel.class）
            .channel(NioServerSocketChannel.class)

            // 指定在服务端启动过程中的逻辑
            .handler(new ChannelInitializer<NioServerSocketChannel>() {
                @Override
                protected void initChannel(NioServerSocketChannel ch) {
                    System.out.println("服务端启动中...");
                }
            })

            // 为 NioServerSocketChannel 指定自定义属性
            .attr(AttributeKey.newInstance("serverName"), "nettyServer")

            // 为 NioServerSocketChannel 设置属性
            .option(ChannelOption.SO_BACKLOG, 1024)

            // 为每条连接指定自定义属性
            .childAttr(AttributeKey.newInstance("clientKey"), "clientValue")

            // 为每条连接设置一些TCP底层相关的属性
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)

            // 定义后续每条连接的数据读写，业务处理逻辑：NioSocketChannel 与 NioServerSocketChannel 的关系类似 Socket 与 ServerSocket
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) {
                    // 添加逻辑处理器
                    // ch.pipeline().addLast(new ServerHandler());
                    ch.pipeline().addLast(new Spliter());
                    ch.pipeline().addLast(new PacketDecoder());
                    ch.pipeline().addLast(new LoginRequestHandler());
                    ch.pipeline().addLast(new MessageRequestHandler());
                    ch.pipeline().addLast(new PacketEncoder());
                }
            })
        ;

        // 异步递归绑定端口
        bind(serverBootstrap, 1000);
    }

    /**
     * 自动绑定递增端口（递归直到端口可用）
     *
     * @param serverBootstrap
     * @param port
     */
    private static void bind(final ServerBootstrap serverBootstrap, final int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口 [" + port + "] 绑定成功!");
            } else {
                System.err.println("端口 [" + port + "] 绑定失败!");
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
