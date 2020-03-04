package com.ywh.demo.io.nio.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 客户端逻辑处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
public class FirstClientHandler extends ChannelInboundHandlerAdapter {

    private static final Integer TASK_COUNT = 1_000;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
//        String content = "Hello, World!";
//        System.out.println(new Date() + ": 客户端写入数据 -> " + content);
//
//        // 获取用于填充二进制数据的 ByteBuf，填充数据（指定字符集为 utf-8）后写入 channel
//        ByteBuf buffer = ctx.alloc().buffer();
//        buffer.writeBytes(content.getBytes(StandardCharsets.UTF_8));
//        ctx.writeAndFlush(buffer);

        // 粘包、半包：多个字符串“粘”在了一起，或字符串被“拆”开
        // 对于操作系统层面，通信采用 TCP 协议，因此使用 ByteBuf 为单位，在操作系统层面仍然是以字节流发送数据
        // 数据到了服务端，是按照字节流的方式读入，在 Netty 应用层面重新拼装成 ByteBuf
        // 而此处的 ByteBuf 与客户端按顺序发送的 ByteBuf 可能不对等
        for (int i = 0; i < TASK_COUNT; i++) {
            ByteBuf buffer = getByteBuf(ctx);
            ctx.writeAndFlush(buffer);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 客户端读取数据 -> " + byteBuf.toString(StandardCharsets.UTF_8));
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        byte[] bytes = "Hello, World!".getBytes(StandardCharsets.UTF_8);
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);

        return buffer;
    }
}
