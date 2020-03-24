package com.ywh.rpc.common;

import io.netty.channel.ChannelHandlerContext;

/**
 * 消息控制器
 *
 * @author ywh
 */
@FunctionalInterface
public interface IMessageHandler<T> {

	/**
	 * 消息处理
	 *
	 * @param ctx
	 * @param requestId
	 * @param message
	 */
	void handle(ChannelHandlerContext ctx, String requestId, T message);

}
