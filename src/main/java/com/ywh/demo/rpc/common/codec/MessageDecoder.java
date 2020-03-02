package com.ywh.demo.rpc.common.codec;

import com.ywh.demo.rpc.common.entity.MessageInput;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.ReplayingDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 消息解码器
 *
 * @author ywh
 */
public class MessageDecoder extends ReplayingDecoder<MessageInput> {

	private static final Integer STR_MAX_LENGTH = (1 << 20);

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
		String requestId = readStr(in);
		String type = readStr(in);
		String content = readStr(in);
		out.add(new MessageInput(type, requestId, content));
	}

	private String readStr(ByteBuf in) {
		int len = in.readInt();
		if (len < 0 || len > STR_MAX_LENGTH) {
			throw new DecoderException("string too long len=" + len);
		}
		byte[] bytes = new byte[len];
		in.readBytes(bytes);
		return new String(bytes, StandardCharsets.UTF_8);
	}

}
