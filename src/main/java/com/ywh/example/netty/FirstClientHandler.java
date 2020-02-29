package com.ywh.example.netty;

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

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        String content = "Hello, World!";
        System.out.println(new Date() + ": 客户端写入数据 -> " + content);

        // 获取用于填充二进制数据的 ByteBuf，填充数据（指定字符集为 utf-8）后写入 channel
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(content.getBytes(StandardCharsets.UTF_8));
        ctx.channel().writeAndFlush(buffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 客户端读取数据 -> " + byteBuf.toString(StandardCharsets.UTF_8));
    }
}
