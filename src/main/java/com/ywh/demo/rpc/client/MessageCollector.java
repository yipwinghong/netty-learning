package com.ywh.demo.rpc.client;

import com.ywh.demo.rpc.common.entity.MessageInput;
import com.ywh.demo.rpc.common.entity.MessageOutput;
import com.ywh.demo.rpc.common.MessageRegistry;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author ywh
 */
@Sharable
public class MessageCollector extends ChannelInboundHandlerAdapter {
	private MessageRegistry registry;
	private RpcClient client;
	private ChannelHandlerContext context;
	private ConcurrentMap<String, RpcFuture<?>> pendingTasks = new ConcurrentHashMap<>();

	private Throwable connectionClosed = new Exception("rpc connection not active error");

	public MessageCollector(MessageRegistry registry, RpcClient client) {
		this.registry = registry;
		this.client = client;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		this.context = ctx;
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		this.context = null;
		pendingTasks.forEach((__, future) -> future.fail(connectionClosed));
		pendingTasks.clear();
		// 尝试重连
		ctx.channel().eventLoop().schedule(() -> client.reconnect(), 1, TimeUnit.SECONDS);
	}

	public <T> RpcFuture<T> send(MessageOutput output) {
		ChannelHandlerContext ctx = context;
		RpcFuture<T> future = new RpcFuture<>();
		if (ctx != null) {
			ctx.channel().eventLoop().execute(() -> {
				pendingTasks.put(output.getRequestId(), future);
				ctx.writeAndFlush(output);
			});
		} else {
			future.fail(connectionClosed);
		}
		return future;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		if (!(msg instanceof MessageInput)) {
			return;
		}
		MessageInput input = (MessageInput) msg;
		// 业务逻辑在这里
		Class<?> clazz = registry.get(input.getType());
		if (clazz == null) {
			System.out.println("unrecognized msg type " + input.getType());
			return;
		}
		Object o = input.getPayload(clazz);
		@SuppressWarnings("unchecked")
		RpcFuture<Object> future = (RpcFuture<Object>) pendingTasks.remove(input.getRequestId());
		if (future == null) {
			System.out.println("future not found with type " + input.getType());
			return;
		}
		future.success(o);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {

	}

	public void close() {
		ChannelHandlerContext ctx = context;
		if (ctx != null) {
			ctx.close();
		}
	}

}
