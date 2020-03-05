package com.ywh.demo.im.server.handler;

import com.ywh.demo.im.protocol.request.CreateGroupRequestPacket;
import com.ywh.demo.im.protocol.response.CreateGroupResponsePacket;
import com.ywh.demo.im.session.SessionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

import java.util.List;
import java.util.UUID;

/**
 * 服务端创建群组请求处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
@ChannelHandler.Sharable
public class CreateGroupRequestHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {

    public static final CreateGroupRequestHandler INSTANCE = new CreateGroupRequestHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket createGroupRequestPacket) {
        List<String> userNameList = createGroupRequestPacket.getUserNameList();
        // 创建一个 channel 分组
        ChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());

        // 筛选出待加入群聊的用户的 channel 和 userName
        for (String userName : userNameList) {
            Channel channel = SessionUtil.getChannel(userName);
            if (channel != null) {
                channelGroup.add(channel);

            }
        }

        // 创建群聊创建结果的响应
        CreateGroupResponsePacket createGroupResponsePacket = new CreateGroupResponsePacket();
        createGroupResponsePacket.setSuccess(true);
        createGroupResponsePacket.setGroupName(UUID.randomUUID().toString().split("-")[0]);
        createGroupResponsePacket.setUserNameList(userNameList);

        // 给每个客户端发送拉群通知
        channelGroup.writeAndFlush(createGroupResponsePacket);

        System.out.print("群创建成功，id 为 [" + createGroupResponsePacket.getGroupName() + "]，");
        System.out.println("群成员有：" + createGroupResponsePacket.getUserNameList());

    }
}
