package com.ywh.io.nio.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;

/**
 * 通过 {@link ChannelInitializer} 的 initChannel 取得 channel 对应的 pipeline，
 * 利用 channelRegistered() 和 handlerAdded() 添加 handler
 *
 *
 * @author ywh
 */
public class LifeCyCleTestHandler extends ChannelInboundHandlerAdapter {

    /**
     * 用于资源的申请
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("1. handlerAdded()：逻辑处理器被添加");
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("2. channelRegistered()：channel 绑定到线程 NioEventLoop");
        super.channelRegistered(ctx);
    }

    /**
     * TCP 连接建立，可用于统计连接数、实现 IP 黑白名单过滤
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("3. channelActive()：channel 准备就绪");
        super.channelActive(ctx);
    }

    /**
     * 根据自定义协议进行拆包
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("4. channelRead()：channel 有数据可读");
        super.channelRead(ctx, msg);
    }

    /**
     * writeAndFlush 效率比较低，可以用 write + ctx.channel().flush() 批量刷新
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("5. channelReadComplete()：channel 某次数据读完");
        super.channelReadComplete(ctx);

    }

    /**
     * TCP 连接断开，可用于统计连接数
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("6. channelInactive()：channel 被关闭");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("7. channelUnregistered()：channel 取消线程 NioEventLoop 的绑定 ");
        super.channelUnregistered(ctx);
    }

    /**
     * 用于资源的释放
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("8. handlerRemoved()：逻辑处理器被移除");
        super.handlerRemoved(ctx);
    }
}
