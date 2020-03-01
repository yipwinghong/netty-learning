package com.ywh.demo.im.server.handler;

import com.ywh.demo.im.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 身份校验处理器
 *
 * @author ywh
 */
@ChannelHandler.Sharable
public class AuthHandler extends ChannelInboundHandlerAdapter {

    public static final AuthHandler INSTANCE = new AuthHandler();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!SessionUtil.hasLogin(ctx.channel())) {
            ctx.channel().close();
        } else {
            // 如果通过校验则直接调用 pipeline 中的 remove 方法删除自身
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        }
    }

    /**
     *
     * @param ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        if (SessionUtil.hasLogin(ctx.channel())) {
            System.out.println("当前连接登录验证完毕，无需再次验证, AuthHandler 被移除");
        } else {
            System.out.println("无登录验证，强制关闭连接！");
        }
    }
}
