package com.ywh.demo.im.codec;

import com.ywh.demo.im.protocol.BasePacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 编码器（Java 对象 -> 二进制数据）
 * @author ywh
 */
public class PacketEncoder extends MessageToByteEncoder<BasePacket> {

    @Override
    protected void encode(ChannelHandlerContext ctx, BasePacket packet, ByteBuf out) {
        PacketCodeC.encode(out, packet);
    }
}
