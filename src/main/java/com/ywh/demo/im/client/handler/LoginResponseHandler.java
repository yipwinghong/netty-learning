package com.ywh.demo.im.client.handler;

import com.ywh.demo.im.protocol.response.LoginResponsePacket;
import com.ywh.demo.im.session.Session;
import com.ywh.demo.im.session.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Date;

/**
 * 客户端登录响应处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
@ChannelHandler.Sharable
public class LoginResponseHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    public static final LoginResponseHandler INSTANCE = new LoginResponseHandler();

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) {
//        System.out.println(new Date() + ": 客户端登录中...");
//
//        // 创建登录对象
//        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
//        loginRequestPacket.setUserName("ywh");
//        loginRequestPacket.setPassword("pwd");
//
//        // 编码，写入数据
//        ctx.writeAndFlush(loginRequestPacket);
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket loginResponsePacket) {

        // 登录成功，标记登录状态
        if (loginResponsePacket.getSuccess()) {
            SessionUtil.bindSession(new Session(loginResponsePacket.getUserName()), ctx.channel());
             System.out.println(new Date() + ": 客户端登录成功！");
        } else {
            System.out.println(new Date() + ": 客户端登录失败！原因：" + loginResponsePacket.getReason());
        }
    }
}
