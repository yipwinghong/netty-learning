package com.ywh.netty.server;


import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author ywh
 */
@SpringBootApplication
public class NettyServerApplication implements CommandLineRunner {

    @Autowired
    private NettyServer nettyServer;

    /**
     * @param args
     */
    public static void main(String[] args) {
       SpringApplication.run(NettyServerApplication.class);
    }

    @Override
    public void run(String... args) {
        nettyServer.bind();
//        ChannelFuture channelFuture = nettyServer.bind();
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> nettyServer.destroy()));
//        channelFuture.channel().closeFuture().syncUninterruptibly();
    }
}
