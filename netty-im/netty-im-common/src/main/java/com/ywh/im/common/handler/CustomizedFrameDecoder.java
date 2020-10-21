package com.ywh.im.common.handler;

import com.ywh.im.common.protocol.PacketCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import static com.ywh.im.common.constant.Constant.*;

/**
 * 由于 TCP 是基于流的协议，应用层 ByteBuf 在底层是按照字节流发送数据、在接收端把字节流重新拼装成 ByteBuf，因此两端的 ByteBuf 可能是不对等的（受限于缓冲区大小、MTU 等）。
 * 需要自行定义拆包规则，原理是不断从 TCP 缓冲区中读取数据，每次读取完都判断是否是一个完整的数据包（保留数据拼接直到完整）。
 * Netty 自带拆包器：
 *      固定长度的拆包器     FixedLengthFrameDecoder
 *      行拆包器            LineBasedFrameDecoder
 *      分隔符拆包器         DelimiterBasedFrameDecoder
 *      基于长度域拆包器     LengthFieldBasedFrameDecoder
 * 自定义拆包器，基于长度域拆包器上扩展，添加拒绝非本协议连接的逻辑（根据魔数）。
 *
 * @author ywh
 * @since 24/12/2019
 */
public class CustomizedFrameDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * 偏移量
     */
    private static final Integer LENGTH_FIELD_OFFSET = 7;

    /**
     * 包长度
     */
    private static final Integer LENGTH_FIELD_LENGTH = 4;

    /**
     * 设置包长度和偏移量，其中通信协议参考 {@link PacketCodec}。
     */
    public CustomizedFrameDecoder() {
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
        // 拒绝非本协议连接会被关闭：读取前 4bytes，如不等于 MAGIC_NUMBER 则返回错误。
        if (in.getInt(in.readerIndex()) != MAGIC_NUMBER) {
            ctx.channel().close();
            return null;
        }
        return super.decode(ctx, in);
    }
}
