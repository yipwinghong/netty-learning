package com.ywh.demo.im.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;


/**
 * 空闲检测控制器（避免连接假死）
 *
 * @author ywh
 */
public class ImIdleStateHandler extends IdleStateHandler {

    private static final int READER_IDLE_TIME = 15;

    public ImIdleStateHandler() {
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        System.out.println(READER_IDLE_TIME + " 秒内未读到数据，关闭连接");
        ctx.channel().close();
    }
}
