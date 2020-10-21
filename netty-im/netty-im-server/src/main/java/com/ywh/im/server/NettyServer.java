package com.ywh.im.server;

import com.ywh.im.common.handler.*;
import com.ywh.im.server.handler.AuthHandler;
import com.ywh.im.server.handler.HeartBeatRequestHandler;
import com.ywh.im.server.handler.ImHandler;
import com.ywh.im.server.handler.LoginRequestHandler;
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
    public static void main(String[] args) throws InterruptedException {
        // parentGroup 用于接收请求创建连接，childGroup 用于读取数据处理业务逻辑。
        NioEventLoopGroup parentGroup = new NioEventLoopGroup(), childGroup = new NioEventLoopGroup();

        // 引导服务端启动工作
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap
                // 配置两个线程组
                .group(parentGroup, childGroup)

                // 指定 NIO 模型（BIO 则对应 OioServerSocketChannel.class）
                .channel(NioServerSocketChannel.class)

                // 指定在服务端启动过程中的逻辑
                .handler(
                    // ChannelInitializer 用于配置一个新的 Channel。
                    new ChannelInitializer<NioServerSocketChannel>() {
                        @Override
                        protected void initChannel(NioServerSocketChannel ch) {
                            System.out.println("服务端启动中...");
                        }
                    }
                )
                // 为 NioServerSocketChannel 设置选项和自定义属性：比如 TCP/IP 的服务端，常用 socket 的参数选项 tcpNoDelay、keepAlive 等。
                // 属性配置项可参考接口文档：
                // https://netty.io/4.0/api/io/netty/channel/ChannelOption.html
                // https://netty.io/4.0/api/io/netty/channel/ChannelConfig.html
                .option(ChannelOption.SO_BACKLOG, 1024)
                .attr(AttributeKey.newInstance("serverName"), "nettyServer")

                // 为每条连接指定自定义属性和选项。
                .childAttr(AttributeKey.newInstance("clientKey"), "clientValue")
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)

                // 定义后续每条连接的数据读写，业务处理逻辑：NioSocketChannel 与 NioServerSocketChannel 的关系类似 Socket 与 ServerSocket
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {

                        // 空闲检测必须放在最前面，否则在连接读到数据时，在 inBound 传播的过程中出错或者数据处理完毕就不往后传递，最终 ImIdleStateHandler 不会读到数据、导致误判
                        ch.pipeline()
                            .addLast(new ImIdleStateHandler())
                            .addLast(new CustomizedFrameDecoder())
                            .addLast(PacketCodecHandler.INSTANCE)
                            .addLast(LoginRequestHandler.INSTANCE)
                            .addLast(HeartBeatRequestHandler.INSTANCE)
                            .addLast(AuthHandler.INSTANCE)
                            .addLast(ImHandler.INSTANCE);
                    }
                })
            ;

            // 异步递归绑定端口
            bind(serverBootstrap, 1000);
        } catch (Exception ex) {
            parentGroup.shutdownGracefully().sync();
            childGroup.shutdownGracefully().sync();
        }

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
                System.out.println("端口 [" + port + "] 绑定成功！");
                return;
            }
            System.err.println("端口 [" + port + "] 绑定失败！");
            bind(serverBootstrap, port + 1);
        });
    }
}
