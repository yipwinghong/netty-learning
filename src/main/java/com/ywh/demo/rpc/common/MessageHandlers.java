package com.ywh.demo.rpc.common;

import com.ywh.demo.rpc.common.entity.MessageInput;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息控制器组
 *
 * @author ywh
 */
public class MessageHandlers {

	private Map<String, IMessageHandler<?>> handlers = new HashMap<>();

	private IMessageHandler<MessageInput> defaultHandler;

	public void register(String type, IMessageHandler<?> handler) {
		handlers.put(type, handler);
	}

	public void defaultHandler(IMessageHandler<MessageInput> defaultHandler) {
		this.defaultHandler = defaultHandler;
	}

	public IMessageHandler<MessageInput> defaultHandler() {
		return defaultHandler;
	}

	public IMessageHandler<?> get(String type) {
		return handlers.get(type);
	}

}
