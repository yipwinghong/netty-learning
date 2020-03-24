package com.ywh.rpc.server;

import com.ywh.rpc.common.IMessageHandler;
import com.ywh.rpc.common.entity.MessageInput;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author ywh
 */
public class DefaultHandler implements IMessageHandler<MessageInput> {

	@Override
	public void handle(ChannelHandlerContext ctx, String requestId, MessageInput input) {

		System.out.println("unrecognized message type " + input.getType() + "comes");
		ctx.close();
	}

}
