package com.ywh.im.client;

import com.ywh.im.client.console.ConsoleCommandManager;
import com.ywh.im.client.console.LoginConsoleCommand;
import com.ywh.im.client.handler.*;
import com.ywh.im.handler.SplitterHandler;
import com.ywh.im.handler.ImIdleStateHandler;
import com.ywh.im.handler.PacketCodecHandler;
import com.ywh.im.session.SessionUtil;
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
import java.util.concurrent.*;

/**
 * 客户端启动流程
 *
 * @author ywh
 * @since 24/12/2019
 */
public class NettyClient {

    private static final Integer CLIENT_CONNECT_MAX_RETRY = 5;

    private static ExecutorService executorService;

    static {
        executorService = Executors.newCachedThreadPool();
    }

    public static void main(String[] args) throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap
                // 指定线程模型，IO 类型为 NIO
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    /**
                     * 指定连接数据读写逻辑（责任链模式）
                     *
                     * @param ch
                     */
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new ImIdleStateHandler());
                        ch.pipeline().addLast(new SplitterHandler());
                        ch.pipeline().addLast(PacketCodecHandler.INSTANCE);

                        // 登录响应处理器
                        ch.pipeline().addLast(new LoginResponseHandler());
                        // 收消息处理器
                        ch.pipeline().addLast(new MessageResponseHandler());
                        // 创建群响应处理器
                        ch.pipeline().addLast(new CreateGroupResponseHandler());
                        // 加群响应处理器
                        ch.pipeline().addLast(new JoinGroupResponseHandler());
                        // 退群响应处理器
                        ch.pipeline().addLast(new QuitGroupResponseHandler());
                        // 获取群成员响应处理器
                        ch.pipeline().addLast(new ListGroupMembersResponseHandler());
                        // 登出响应处理器
                        ch.pipeline().addLast(new LogoutResponseHandler());

                        ch.pipeline().addLast(new HeartBeatTimerHandler());
                    }
                })
                .attr(AttributeKey.newInstance("clientName"), "nettyClient")
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
            ;
            // 建立连接
            connect(bootstrap, "localhost", 1000, CLIENT_CONNECT_MAX_RETRY);
        } catch (Exception ex) {
            group.shutdownGracefully().sync();
        }

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
        bootstrap.connect(host, port)
            .addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("连接成功！");
                    Channel channel = ((ChannelFuture) future).channel();
                    startConsoleThread(channel);
                } else if (retry == 0) {
                    System.err.println("重试结束！");
                } else {
                    // 第几次重连
                    int order = (CLIENT_CONNECT_MAX_RETRY - retry) + 1;

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
        Scanner sc = new Scanner(System.in);
        ConsoleCommandManager consoleCommandManager = new ConsoleCommandManager();
        LoginConsoleCommand loginConsoleCommand = new LoginConsoleCommand();

        executorService.execute(
            () -> {
                // 从控制台获取消息之后，将消息封装成消息对象、编码成 ByteBuf，将消息写到服务端
                while (!Thread.interrupted()) {
                    if (!SessionUtil.hasLogin(channel)) {
                        loginConsoleCommand.exec(sc, channel);
                    } else {
                        consoleCommandManager.exec(sc, channel);
                    }
                }
            }
        );

//        new Thread(() -> {
//
//        }).start();
    }
}
