package com.ywh.rpc.example;

import com.ywh.rpc.example.handler.ExpRequestHandler;
import com.ywh.rpc.example.handler.FibRequestHandler;
import com.ywh.rpc.example.request.ExpRequest;
import com.ywh.rpc.server.RpcServer;


/**
 * @author ywh
 */
public class DemoServer {

	public static void main(String[] args) {
		RpcServer server = new RpcServer("localhost", 8888, 2, 16);
		server.service("fib", Integer.class, new FibRequestHandler()).service("exp", ExpRequest.class, new ExpRequestHandler());
		server.start();
	}

}
