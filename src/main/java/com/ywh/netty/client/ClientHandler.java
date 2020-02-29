package com.ywh.netty.client;

import com.ywh.netty.codec.PacketCodeC;
import com.ywh.netty.protocol.Packet;
import com.ywh.netty.protocol.request.LoginRequestPacket;
import com.ywh.netty.protocol.response.LoginResponsePacket;
import com.ywh.netty.protocol.response.MessageResponsePacket;
import com.ywh.netty.util.LoginUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;
import java.util.UUID;

/**
 * 客户端处理器
 *
 * @author ywh
 * @since 29/02/2020
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.decode(byteBuf);

        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            // 登录成功，标记登录状态
            if (loginResponsePacket.isSuccess()) {
                LoginUtil.markAsLogin(ctx.channel());
                System.out.println(new Date() + ": 客户端登录成功！");
            } else {
                System.out.println(new Date() + ": 客户端登录失败！原因：" + loginResponsePacket.getReason());
            }
        } else if (packet instanceof MessageResponsePacket) {
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            System.out.println(new Date() + ": 接收到服务端的消息: " + messageResponsePacket.getMessage());
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(new Date() + ": 客户端登录中...");

        // 创建登录对象
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserId(UUID.randomUUID().toString());
        loginRequestPacket.setUsername("ywh");
        loginRequestPacket.setPassword("pwd");

        // 编码，写入数据
        ctx.channel().writeAndFlush(PacketCodeC.encode(ctx.alloc(), loginRequestPacket));
    }
}
