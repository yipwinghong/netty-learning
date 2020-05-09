package com.ywh.http;

import com.ywh.http.handler.HttpServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ywh
 * @since 2020/3/24/024
 */
@Slf4j
public class HttpServer {
    private static final int PORT = 8888;

    public static void main(String[] args) throws Exception {

        EventLoopGroup parentGroup = new NioEventLoopGroup(1), childGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel c) {
                        c.pipeline()
                            .addLast(new HttpServerCodec())     // 或者使用 HttpRequestDecoder & HttpResponseEncoder
                            .addLast(new HttpObjectAggregator(1024 * 1024))   // 处理 POST 请求
                            .addLast(new HttpServerExpectContinueHandler())
                            .addLast(new HttpServerHandler());
                    }
                });

            Channel c = b.bind(PORT).sync().channel();
            log.info("Netty http server listening on port " + PORT);
            c.closeFuture().sync();
        } finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
