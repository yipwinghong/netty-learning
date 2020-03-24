package com.ywh.rpc.server;

import com.ywh.rpc.common.codec.MessageDecoder;
import com.ywh.rpc.common.codec.MessageEncoder;
import com.ywh.rpc.common.IMessageHandler;
import com.ywh.rpc.common.MessageHandlers;
import com.ywh.rpc.common.MessageRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author ywh
 */
public class RpcServer {

	private String ip;

	private int port;

	private int ioThreads;

	private int workerThreads;

	private MessageHandlers handlers = new MessageHandlers();

	private MessageRegistry registry = new MessageRegistry();

	{
		handlers.defaultHandler(new DefaultHandler());
	}

	public RpcServer(String ip, int port, int ioThreads, int workerThreads) {
		this.ip = ip;
		this.port = port;
		this.ioThreads = ioThreads;
		this.workerThreads = workerThreads;
	}

	private EventLoopGroup group;
	private MessageCollector collector;
	private Channel serverChannel;

	public RpcServer service(String type, Class<?> reqClass, IMessageHandler<?> handler) {
		registry.register(type, reqClass);
		handlers.register(type, handler);
		return this;
	}

	public void start() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		group = new NioEventLoopGroup(ioThreads);
		bootstrap.group(group);
		collector = new MessageCollector(handlers, registry, workerThreads);
		MessageEncoder encoder = new MessageEncoder();
		bootstrap.channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			public void initChannel(SocketChannel ch) {
				ChannelPipeline pipe = ch.pipeline();
				pipe.addLast(new ReadTimeoutHandler(60));
				pipe.addLast(new MessageDecoder());
				pipe.addLast(encoder);
				pipe.addLast(collector);
			}
		});
		bootstrap
			.option(ChannelOption.SO_BACKLOG, 100)
			.option(ChannelOption.SO_REUSEADDR, true)
			.option(ChannelOption.TCP_NODELAY, true)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
		serverChannel = bootstrap.bind(this.ip, this.port).channel();
		System.out.println("server started @ " + ip + ":" + port);
	}

	public void stop() {
		// 先关闭服务端套件字
		serverChannel.close();
		// 再斩断消息来源，停止io线程池
		group.shutdownGracefully();
		// 最后停止业务线程
		collector.closeGracefully();
	}

}