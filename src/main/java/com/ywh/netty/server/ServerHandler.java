package com.ywh.netty.server;

import com.ywh.netty.codec.PacketCodeC;
import com.ywh.netty.protocol.Packet;
import com.ywh.netty.protocol.request.LoginRequestPacket;
import com.ywh.netty.protocol.request.MessageRequestPacket;
import com.ywh.netty.protocol.response.LoginResponsePacket;
import com.ywh.netty.protocol.response.MessageResponsePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandler;

import java.util.Date;

/**
 * 服务端处理器
 * {@link ChannelInboundHandler} 定义读数据的逻辑（解析），在 channel pipeline 中执行顺序为顺序（channelRead）
 * {@link ChannelOutboundHandler} 定义写数据的逻辑（封装），在 channel pipeline 中执行顺序为倒序（write）
 *
 * 在一个 channel pipeline 中 handler 以双向链表的形式存放；
 * 如果按 InA, InB, OutA, OutB 的顺序添加，则执行顺序为：InA -> InB -> OutB -> Out A；
 * 虽然存放在同一个双向链表，但两类 handler 分工不同，事件传播的规则是：InA 通常只会传播到下一个 InA，OutA 只会传播到下一个 OutA，互不干扰。
 *
 * @author ywh
 * @since 29/02/2020
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf requestByteBuf = (ByteBuf) msg;

        // 解码
        Packet packet = PacketCodeC.decode(requestByteBuf);

        // 判断是否是登录请求数据包
        if (packet instanceof LoginRequestPacket) {
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setVersion(packet.getVersion());
            if (valid(loginRequestPacket)) {
                loginResponsePacket.setSuccess(true);

                System.out.println(new Date() + ": 用户 ["+ loginRequestPacket.getUsername() +"] 登录成功！");
            } else {
                loginResponsePacket.setReason("账号密码校验失败");
                loginResponsePacket.setSuccess(false);
                System.out.println(new Date() + ": 登录失败！");
            }
            // 登录响应
            ByteBuf responseByteBuf = PacketCodeC.encode(ctx.alloc(), loginResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        } else if (packet instanceof MessageRequestPacket) {
            MessageRequestPacket messageRequestPacket = ((MessageRequestPacket) packet);
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端已接收到消息 [" + messageRequestPacket.getMessage() + "]");
            ByteBuf responseByteBuf = PacketCodeC.encode(ctx.alloc(), messageResponsePacket);
            ctx.channel().writeAndFlush(responseByteBuf);
        }
    }

    private boolean valid(LoginRequestPacket loginRequestPacket) {
        return true;
    }
}
