package com.ywh.im.server.handler;

import com.ywh.im.common.protocol.request.GroupMessageRequestPacket;
import com.ywh.im.common.protocol.response.GroupMessageResponsePacket;
import com.ywh.im.common.session.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;

/**
 * 服务端群组消息请求处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
@ChannelHandler.Sharable
public class GroupMessageRequestHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    public static final GroupMessageRequestHandler INSTANCE = new GroupMessageRequestHandler();

    private GroupMessageRequestHandler() {

    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket requestPacket) {
        // 拿到 groupName 构造群聊消息的响应
        String groupName = requestPacket.getToGroupName();
        GroupMessageResponsePacket responsePacket = new GroupMessageResponsePacket();
        responsePacket.setFromGroupName(groupName);
        responsePacket.setMessage(requestPacket.getMessage());
        responsePacket.setFromUser(SessionUtil.getSession(ctx.channel()));

        // 拿到群聊对应的 channelGroup，写到每个客户端
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupName);
        channelGroup.writeAndFlush(responsePacket);
    }
}
