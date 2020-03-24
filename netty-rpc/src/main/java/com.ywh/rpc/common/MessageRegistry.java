package com.ywh.rpc.common;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息注册
 *
 * @author ywh
 */
public class MessageRegistry {

	private Map<String, Class<?>> clazzes = new HashMap<>();

	public void register(String type, Class<?> clazz) {
		clazzes.put(type, clazz);
	}

	public Class<?> get(String type) {
		return clazzes.get(type);
	}
}
