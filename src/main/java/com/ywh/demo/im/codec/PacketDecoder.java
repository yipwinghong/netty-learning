package com.ywh.demo.im.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 解码器（二进制数据 -> Java 对象）
 *
 * @author ywh
 */
public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) {
        // out 中添加解码后的结果对象，可自动实现结果往下一个 handler 传递
        out.add(PacketCodec.INSTANCE.decode(in));
    }
}
