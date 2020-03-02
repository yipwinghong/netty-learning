package com.ywh.demo.rpc.server;

import com.ywh.demo.rpc.common.IMessageHandler;
import com.ywh.demo.rpc.common.entity.MessageInput;
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
