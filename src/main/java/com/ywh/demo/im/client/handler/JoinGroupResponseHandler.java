package com.ywh.demo.im.client.handler;

import com.ywh.demo.im.protocol.response.JoinGroupResponsePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端加入群组响应处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
@ChannelHandler.Sharable
public class JoinGroupResponseHandler extends SimpleChannelInboundHandler<JoinGroupResponsePacket> {

    public static final JoinGroupResponseHandler INSTANCE = new JoinGroupResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponsePacket responsePacket) {
        if (responsePacket.isSuccess()) {
            System.out.println("加入群 [" + responsePacket.getGroupName() + "] 成功!");
        } else {
            System.err.println("加入群 [" + responsePacket.getGroupName() + "] 失败，原因为：" + responsePacket.getReason());
        }
    }
}
