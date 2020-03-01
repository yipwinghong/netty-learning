package com.ywh.demo.im.client.handler;

import com.ywh.demo.im.protocol.response.CreateGroupResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端创建群组响应处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
@ChannelHandler.Sharable
public class CreateGroupResponseHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    public static final CreateGroupResponseHandler INSTANCE = new CreateGroupResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket createGroupResponsePacket) {
        System.out.print("群创建成功，id 为 [" + createGroupResponsePacket.getGroupName() + "]，");
        System.out.println("群成员：" + createGroupResponsePacket.getUserNameList());
    }
}
