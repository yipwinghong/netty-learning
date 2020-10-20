package com.ywh.im.client.handler;

import com.ywh.im.common.protocol.response.QuitGroupResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端退出群组响应处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
@ChannelHandler.Sharable
public class QuitGroupResponseHandler extends SimpleChannelInboundHandler<QuitGroupResponsePacket> {

    public static final QuitGroupResponseHandler INSTANCE = new QuitGroupResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupResponsePacket responsePacket) {
        if (responsePacket.isSuccess()) {
            System.out.println("退出群聊 [" + responsePacket.getGroupName() + "] 成功！");
        } else {
            System.out.println("退出群聊 [" + responsePacket.getGroupName() + "] 失败！");
        }

    }
}
