package com.ywh.im.server.handler;

import com.ywh.im.protocol.request.MessageRequestPacket;
import com.ywh.im.protocol.response.MessageResponsePacket;
import com.ywh.im.session.Session;
import com.ywh.im.session.SessionUtil;
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
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        Channel toUserChannel = SessionUtil.getChannel(messageRequestPacket.getToUserName());
        if (SessionUtil.hasLogin(toUserChannel)) {
            Session session = SessionUtil.getSession(ctx.channel());
            messageResponsePacket.setFromUserName(session.getUserName());
            messageResponsePacket.setMessage(messageRequestPacket.getMessage());
            toUserChannel.writeAndFlush(messageResponsePacket);
        } else {
            messageResponsePacket.setMessage("用户 [" + messageRequestPacket.getToUserName() + "] 不在线，发送失败！");
            messageResponsePacket.setFromUserName("服务端");
            ctx.writeAndFlush(messageResponsePacket);
        }
    }
}
