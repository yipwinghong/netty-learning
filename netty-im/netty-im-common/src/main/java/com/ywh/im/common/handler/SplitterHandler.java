package com.ywh.im.common.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import static com.ywh.im.common.constant.Constant.*;

/**
 * 自定义拆包器，基于长度域拆包器上扩展，添加拒绝非本协议连接的逻辑（根据魔数）
 *
 * @author ywh
 * @since 24/12/2019
 */
public class SplitterHandler extends LengthFieldBasedFrameDecoder {

    private static final Integer LENGTH_FIELD_OFFSET = 7;

    private static final Integer LENGTH_FIELD_LENGTH = 4;

    public SplitterHandler() {
        super(Integer.MAX_VALUE, LENGTH_FIELD_OFFSET, LENGTH_FIELD_LENGTH);
    }

    /**
     *
     * @param ctx
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {

        // 拒绝非本协议连接会被关闭：读取 4bytes，如不等于 MAGIC_NUMBER 则返回错误
        if (in.getInt(in.readerIndex()) != MAGIC_NUMBER) {
            ctx.channel().close();
            return null;
        }

        return super.decode(ctx, in);
    }
}
