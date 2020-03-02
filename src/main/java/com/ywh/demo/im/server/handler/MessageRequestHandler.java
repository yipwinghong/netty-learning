package com.ywh.demo.im.server.handler;

import com.ywh.demo.im.protocol.request.MessageRequestPacket;
import com.ywh.demo.im.protocol.response.MessageResponsePacket;
import com.ywh.demo.im.session.Session;
import com.ywh.demo.im.util.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * 服务端消息请求处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {

    public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) {
        System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

        Session session = SessionUtil.getSession(ctx.channel());
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setFromUserName(session.getUserName());
        messageResponsePacket.setMessage(messageRequestPacket.getMessage());
         messageResponsePacket.setMessage("服务端已接收到消息 [" + messageRequestPacket.getMessage() + "]");

        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserName());
        if (SessionUtil.hasLogin(toUserChannel)) {
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            System.err.println("用户 [" + messageRequestPacket.getToUserName() + "] 不在线，发送失败！");
        }

        // ctx.channel().writeAndFlush(messageResponsePacket);
    }
}
