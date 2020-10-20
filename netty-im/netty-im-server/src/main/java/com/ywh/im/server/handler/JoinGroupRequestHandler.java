package com.ywh.im.server.handler;

import com.ywh.im.common.protocol.request.JoinGroupRequestPacket;
import com.ywh.im.common.protocol.response.JoinGroupResponsePacket;
import com.ywh.im.common.session.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * 服务端加入群组请求处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
@ChannelHandler.Sharable
public class JoinGroupRequestHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {

    public static final JoinGroupRequestHandler INSTANCE = new JoinGroupRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket requestPacket) {
        // 获取群对应的 channelGroup，然后将当前用户的 channel 添加进去
        String groupName = requestPacket.getGroupName();
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupName);
        channelGroup.add(ctx.channel());

        // 构造加群响应发送给客户端
        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();

        responsePacket.setSuccess(true);
        responsePacket.setGroupName(groupName);
        ctx.writeAndFlush(responsePacket);
    }
}
