package com.ywh.demo.rpc.server;

import com.ywh.demo.rpc.common.IMessageHandler;
import com.ywh.demo.rpc.common.MessageHandlers;
import com.ywh.demo.rpc.common.entity.MessageInput;
import com.ywh.demo.rpc.common.MessageRegistry;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ywh
 */
@Sharable
public class MessageCollector extends ChannelInboundHandlerAdapter {

    private ThreadPoolExecutor executor;

    private MessageHandlers handlers;

    private MessageRegistry registry;

    public MessageCollector(MessageHandlers handlers, MessageRegistry registry, int workerThreads) {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1000);
        ThreadFactory factory = new ThreadFactory() {

            AtomicInteger seq = new AtomicInteger();

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("rpc-" + seq.getAndIncrement());
                return t;
            }

        };
        this.executor = new ThreadPoolExecutor(1, workerThreads, 30, TimeUnit.SECONDS, queue, factory,
			new CallerRunsPolicy());
        this.handlers = handlers;
        this.registry = registry;
    }

    public void closeGracefully() {
        this.executor.shutdown();
        try {
            this.executor.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException ignored) {
        }
        this.executor.shutdownNow();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("connection comes");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("connection leaves");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof MessageInput) {
            this.executor.execute(() -> this.handleMessage(ctx, (MessageInput) msg));
        }
    }

    private void handleMessage(ChannelHandlerContext ctx, MessageInput input) {
        // 业务逻辑在这里
        Class<?> clazz = registry.get(input.getType());
        if (clazz == null) {
            handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
            return;
        }
        Object o = input.getPayload(clazz);
        @SuppressWarnings("unchecked")
        IMessageHandler<Object> handler = (IMessageHandler<Object>) handlers.get(input.getType());
        if (handler != null) {
            handler.handle(ctx, input.getRequestId(), o);
        } else {
            handlers.defaultHandler().handle(ctx, input.getRequestId(), input);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("connection error: " + cause);
    }

}
