package com.ywh.demo.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 服务端逻辑处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        // 接收从客户端接收的数据，关于 ByteBuf 的介绍，参考：https://juejin.im/book/5b4bc28bf265da0f60130116/section/5b4db03b6fb9a04fe91a6e93
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 服务端读取数据 -> " + byteBuf.toString(StandardCharsets.UTF_8));

        // 回写数据给客户端
        // 注意 ByteBuf 使用堆外内存，不被 JVM 管理，如果没有手工释放会造成内存泄漏
    }
}