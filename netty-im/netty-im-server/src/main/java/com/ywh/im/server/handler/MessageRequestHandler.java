package com.ywh.im.server.handler;

import com.ywh.im.common.protocol.request.MessageRequestPacket;
import com.ywh.im.common.protocol.response.MessageResponsePacket;
import com.ywh.im.common.session.Session;
import com.ywh.im.common.session.SessionUtil;
import io.netty.channel.*;

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
            // writeAndFlush 返回一个 ChannelFuture 对象，表示一个异步的 I/O 操作，可以在其后添加监听器、收集写请求执行结果。
            toUserChannel.writeAndFlush(messageResponsePacket);
            // .addListener(ChannelFutureListener.CLOSE);
        } else {
            messageResponsePacket.setMessage("用户 [" + messageRequestPacket.getToUserName() + "] 不在线，发送失败！");
            messageResponsePacket.setFromUserName("服务端");
            ctx.writeAndFlush(messageResponsePacket);
        }
    }
}
