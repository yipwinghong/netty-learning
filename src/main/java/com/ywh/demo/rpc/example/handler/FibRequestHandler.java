package com.ywh.demo.rpc.example.handler;

import com.ywh.demo.rpc.common.IMessageHandler;
import com.ywh.demo.rpc.common.entity.MessageOutput;
import io.netty.channel.ChannelHandlerContext;

import java.util.ArrayList;
import java.util.List;

/**
 * 斐波那契数计算请求控制器
 *
 * @author ywh
 * @since 02/03/2020
 */
public class FibRequestHandler implements IMessageHandler<Integer> {

    private List<Long> fibs = new ArrayList<>();

    {
        // fib(1) = fib(0) = 1
        fibs.add(1L);
        fibs.add(1L);
    }

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, Integer n) {
        for (int i = fibs.size(); i < n + 1; i++) {
            long value = fibs.get(i - 2) + fibs.get(i - 1);
            fibs.add(value);
        }
        ctx.writeAndFlush(new MessageOutput(requestId, "fib_res", fibs.get(n)));
    }

}



