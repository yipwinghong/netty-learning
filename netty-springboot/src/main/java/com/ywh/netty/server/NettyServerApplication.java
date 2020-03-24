package com.ywh.netty.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


/**
 * @author ywh
 */
@SpringBootApplication
public class NettyServerApplication {
    /**
     * @param args
     */
    public static void main(String[] args) {
       SpringApplication.run(NettyServerApplication.class);
    }
}
