package com.ywh.im.server.handler;

import com.ywh.im.protocol.request.QuitGroupRequestPacket;
import com.ywh.im.protocol.response.QuitGroupResponsePacket;
import com.ywh.im.session.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * 服务端退出群组请求处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
@ChannelHandler.Sharable
public class QuitGroupRequestHandler extends SimpleChannelInboundHandler<QuitGroupRequestPacket> {

    public static final QuitGroupRequestHandler INSTANCE = new QuitGroupRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupRequestPacket requestPacket) {
        // 获取群对应的 channelGroup，然后将当前用户的 channel 移除
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(requestPacket.getGroupName());
        channelGroup.remove(ctx.channel());

        // 构造退群响应发送给客户端
        QuitGroupResponsePacket responsePacket = new QuitGroupResponsePacket();

        responsePacket.setGroupName(requestPacket.getGroupName());
        responsePacket.setSuccess(true);
        ctx.writeAndFlush(responsePacket);

    }
}
