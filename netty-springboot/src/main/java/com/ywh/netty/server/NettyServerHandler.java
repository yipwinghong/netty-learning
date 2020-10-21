package com.ywh.netty.server;


import com.ywh.netty.protobuf.UserMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ywh
 */
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 空闲次数
     */
    private AtomicInteger idleCount = new AtomicInteger(1);
    /**
     * 发送次数
     */
    private AtomicInteger count = new AtomicInteger(1);

    /**
     * 建立连接时，发送一条消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("连接的客户端地址：" + ctx.channel().remoteAddress());
        UserMsg.User user = UserMsg.User
            .newBuilder()
            .setId(1)
            .setAge(24)
            .setName("ywh")
            .setState(0)
            .build();
        ctx.writeAndFlush(user);
        super.channelActive(ctx);
    }

    /**
     * 超时处理 如果 5 秒没有接收客户端的心跳则触发超时，如果超过两次超时则直接关闭;
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            // 如果读通道处于空闲状态，说明没有接收到心跳命令
            if (IdleState.READER_IDLE.equals(event.state())) {
                log.info("已经 5 秒没有接收到客户端的信息了");
                if (idleCount.get() > 1) {
                    log.info("关闭这个不活跃的 channel");
                    ctx.channel().close();
                }
                idleCount.getAndIncrement();
            }
        } else {
            super.userEventTriggered(ctx, obj);
        }
    }

    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        log.info("服务端第 " + count.get() + " 次接收的消息：" + msg);
        try {
            // 如果是protobuf类型的数据
            if (msg instanceof UserMsg.User) {
                UserMsg.User user = (UserMsg.User) msg;
                if (user.getState() == 1) {
                    log.info("客户端业务处理成功！");
                } else if (user.getState() == 2) {
                    log.info("接收到客户端发送的心跳！");
                } else {
                    log.info("未知命令！");
                }
            } else {
                log.info("未知数据：" + msg);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放 ByteBuf 对象：由于 ByteBuf 是一个引用计数对象（ReferenceCounted），必须显式释放。
            ReferenceCountUtil.release(msg);
        }
        count.getAndIncrement();
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
