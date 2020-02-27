package com.ywh.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 4. 服务启动流程
 *
 * @author ywh
 * @since 24/12/2019
 */
public class NettyServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap
            // 配置两大线程组
            .group(bossGroup, workerGroup)
            // 指定 NIO 模型（BIO: OioServerSocketChannel.class）
            .channel(NioServerSocketChannel.class)

            // 指定在服务端启动过程中的逻辑
            .handler(new ChannelInitializer<NioServerSocketChannel>() {
                @Override
                protected void initChannel(NioServerSocketChannel ch) {
                    System.out.println("服务端启动中");
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

            // 定义后续每条连接的数据读写，业务处理逻辑（NioSocketChannel 与 NioServerSocketChannel 的关系类似 ServerSocket 与 Socket 的关系）
            .childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) {
                }
            })
        ;

        serverBootstrap.bind(8000);
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
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
