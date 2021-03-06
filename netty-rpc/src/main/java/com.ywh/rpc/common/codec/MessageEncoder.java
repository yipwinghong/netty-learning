package com.ywh.rpc.common.codec;

import com.alibaba.fastjson.JSON;
import com.ywh.rpc.common.entity.MessageOutput;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 消息编码器
 *
 * @author ywh
 */
@Sharable
public class MessageEncoder extends MessageToMessageEncoder<MessageOutput> {

	@Override
	protected void encode(ChannelHandlerContext ctx, MessageOutput msg, List<Object> out) {
		ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer();
		writeStr(buf, msg.getRequestId());
		writeStr(buf, msg.getType());
		writeStr(buf, JSON.toJSONString(msg.getPayload()));
		out.add(buf);
	}

	private void writeStr(ByteBuf buf, String s) {
		buf.writeInt(s.length());
		buf.writeBytes(s.getBytes(StandardCharsets.UTF_8));
	}

}
