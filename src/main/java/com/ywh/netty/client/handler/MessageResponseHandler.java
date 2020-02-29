package com.ywh.netty.client.handler;

import com.ywh.netty.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * 客户端消息响应处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) {
        System.out.println(new Date() + ": 接收到服务端的消息: " + messageResponsePacket.getMessage());
    }
}
