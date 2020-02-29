package com.ywh.netty.client.handler;

import com.ywh.netty.protocol.request.LoginRequestPacket;
import com.ywh.netty.protocol.response.LoginResponsePacket;
import com.ywh.netty.util.LoginUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;
import java.util.UUID;

/**
 * 客户端登录响应处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(new Date() + ": 客户端登录中...");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("ywh");
        loginRequestPacket.setPassword("pwd");

        // 编码，写入数据
        ctx.channel().writeAndFlush(loginRequestPacket);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {

        // 登录成功，标记登录状态
        if (loginResponsePacket.isSuccess()) {
            LoginUtil.markAsLogin(ctx.channel());
            System.out.println(new Date() + ": 客户端登录成功！");
        } else {
            System.out.println(new Date() + ": 客户端登录失败！原因：" + loginResponsePacket.getReason());
        }
    }
}
