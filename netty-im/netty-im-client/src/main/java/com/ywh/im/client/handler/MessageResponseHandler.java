package com.ywh.im.client.handler;

import com.ywh.im.common.protocol.response.MessageResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端消息响应处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
@ChannelHandler.Sharable
public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {

    public static final MessageResponseHandler INSTANCE = new MessageResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) {
//        System.out.println(new Date() + ": 接收到服务端的消息: " + messageResponsePacket.getMessage());
        System.out.println(messageResponsePacket.getFromUserName() + "：" + messageResponsePacket.getMessage());
    }
}
