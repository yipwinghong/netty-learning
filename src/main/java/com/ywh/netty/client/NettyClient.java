package com.ywh.netty.client;

import com.ywh.netty.client.handler.LoginResponseHandler;
import com.ywh.netty.client.handler.MessageResponseHandler;
import com.ywh.netty.codec.PacketDecoder;
import com.ywh.netty.codec.PacketEncoder;
import com.ywh.netty.codec.Spliter;
import com.ywh.netty.protocol.request.MessageRequestPacket;
import com.ywh.netty.util.LoginUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * 客户端启动流程
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
                    // ch.pipeline().addLast(new ClientHandler());
                    ch.pipeline().addLast(new Spliter());
                    ch.pipeline().addLast(new PacketDecoder());
                    ch.pipeline().addLast(new LoginResponseHandler());
                    ch.pipeline().addLast(new MessageResponseHandler());
                    ch.pipeline().addLast(new PacketEncoder());
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
                Channel channel = ((ChannelFuture) future).channel();
                startConsoleThread(channel);
            } else if (retry == 0) {
                System.err.println("重试结束！");
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

    /**
     * 创建一个线程，从控制台读入消息
     *
     * @param channel
     */
    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {

            // 从控制台获取消息之后，将消息封装成消息对象、编码成 ByteBuf，将消息写到服务端
            while (!Thread.interrupted()) {
                if (!LoginUtil.hasLogin(channel)) {
                    continue;
                }
                System.out.println("输入消息发送至服务端: ");
                Scanner sc = new Scanner(System.in);
                String line = sc.nextLine();
                // channel.writeAndFlush(PacketCodeC.encode(channel.alloc(), new MessageRequestPacket(line)));
                channel.writeAndFlush(new MessageRequestPacket(line));
            }
        }).start();
    }
}
