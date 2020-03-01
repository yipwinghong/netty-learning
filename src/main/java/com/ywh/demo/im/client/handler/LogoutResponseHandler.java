package com.ywh.demo.im.client.handler;

import com.ywh.demo.im.protocol.response.LogoutResponsePacket;
import com.ywh.demo.im.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 客户端登出响应处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
@ChannelHandler.Sharable
public class LogoutResponseHandler extends SimpleChannelInboundHandler<LogoutResponsePacket> {

    public static final LogoutResponseHandler INSTANCE = new LogoutResponseHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogoutResponsePacket logoutResponsePacket) {
        SessionUtil.unBindSession(ctx.channel());
    }
}
