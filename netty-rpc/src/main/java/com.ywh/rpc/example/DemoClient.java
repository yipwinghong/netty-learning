package com.ywh.rpc.example;

import com.ywh.rpc.client.RpcClient;
import com.ywh.rpc.client.RpcException;
import com.ywh.rpc.example.request.ExpRequest;
import com.ywh.rpc.example.response.ExpResponse;

/**
 * @author ywh
 */
public class DemoClient {

	private RpcClient client;

	private static final Integer TASK_COUNT = 30;

	public DemoClient(RpcClient client) {
		this.client = client;
		this.client.rpc("fib_res", Long.class).rpc("exp_res", ExpResponse.class);
	}

	public long fib(int n) {
		return (Long) client.send("fib", n);
	}

	public ExpResponse exp(int base, int exp) {
		return (ExpResponse) client.send("exp", new ExpRequest(base, exp));
	}

	public static void main(String[] args) throws InterruptedException {
		RpcClient client = new RpcClient("localhost", 8888);
		DemoClient demo = new DemoClient(client);
		for (int i = 0; i < TASK_COUNT; i++) {
			try {
				System.out.printf("fib(%d) = %d\n", i, demo.fib(i));
				Thread.sleep(100);
			} catch (RpcException e) {
				i--; // retry
			}
		}
		for (int i = 0; i < TASK_COUNT; i++) {
			try {
				ExpResponse res = demo.exp(2, i);
				Thread.sleep(100);
				System.out.printf("exp2(%d) = %d cost=%dns\n", i, res.getValue(), res.getCostInNanos());
			} catch (RpcException e) {
				i--; // retry
			}
		}
		client.close();
	}

}
