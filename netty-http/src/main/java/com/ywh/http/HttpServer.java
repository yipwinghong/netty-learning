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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ywh
 * @since 2020/3/24/024
 */
public class HttpServer {
    private static final Logger log = LoggerFactory.getLogger(HttpServer.class);

    private static final int PORT = 8888;

    public static void main(String[] args) throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.option(ChannelOption.SO_BACKLOG, 1024);
            b.childOption(ChannelOption.TCP_NODELAY,true);
            b.childOption(ChannelOption.SO_KEEPALIVE,true);
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer() {
                    @Override
                    protected void initChannel(Channel ch) {
                        ChannelPipeline p = ch.pipeline();

                        // 或者使用 HttpRequestDecoder & HttpResponseEncoder
                        p.addLast(new HttpServerCodec());

                        // 处理 POST 请求
                        p.addLast(new HttpObjectAggregator(1024*1024));
                        p.addLast(new HttpServerExpectContinueHandler());
                        p.addLast(new HttpServerHandler());
                    }
                });

            Channel ch = b.bind(PORT).sync().channel();
            log.info("Netty http server listening on port "+ PORT);
            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
