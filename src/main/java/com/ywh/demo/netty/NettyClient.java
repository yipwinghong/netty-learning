package com.ywh.demo.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 5. 客户端启动流程
 *
 * @author ywh
 * @since 24/12/2019
 */
public class NettyClient {

    private static int MAX_RETRY = 5;

    public static void main(String[] args) {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap
            // 指定线程模型，IO 类型为 NIO
            .group(new NioEventLoopGroup())
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {

                /**
                 * 指定连接数据读写逻辑（责任链模式）
                 *
                 * @param ch
                 * @throws Exception
                 */
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 添加逻辑处理器
                    ch.pipeline().addLast(new FirstClientHandler());
                }
            })
            .attr(AttributeKey.newInstance("clientName"), "nettyClient")
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .option(ChannelOption.TCP_NODELAY, true)
        ;
        // 建立连接
        connect(bootstrap, "localhost", 1000, MAX_RETRY);
    }

    /**
     * 失败重连（递归 MAX_RETRY 次）
     * 1. 连接成功；
     * 2. 连接失败，已达到最大重试次数
     * 3. 连接失败，未达到最大重试次数
     *
     * @param bootstrap
     * @param host
     * @param port
     */
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;

                // 本次重连的间隔：指数退避策略
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，执行第 " + order + " 次重连...");
                bootstrap.config().group().schedule(
                    () -> connect(bootstrap, host, port, retry - 1), delay, TimeUnit.SECONDS);
            }
        });
    }
}
