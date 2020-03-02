package com.ywh.demo.rpc.example.handler;

import com.ywh.demo.rpc.common.IMessageHandler;
import com.ywh.demo.rpc.common.entity.MessageOutput;
import com.ywh.demo.rpc.example.request.ExpRequest;
import com.ywh.demo.rpc.example.response.ExpResponse;
import io.netty.channel.ChannelHandlerContext;

/**
 * 指数计算请求控制器
 *
 * @author ywh
 * @since 02/03/2020
 */
public class ExpRequestHandler implements IMessageHandler<ExpRequest> {

    @Override
    public void handle(ChannelHandlerContext ctx, String requestId, ExpRequest message) {
        long start = System.nanoTime();
        long res = 1;
        for (int i = 0; i < message.getExp(); i++) {
            res *= message.getBase();
        }
        long cost = System.nanoTime() - start;
        ctx.writeAndFlush(new MessageOutput(requestId, "exp_res", new ExpResponse(res, cost)));
    }

}
