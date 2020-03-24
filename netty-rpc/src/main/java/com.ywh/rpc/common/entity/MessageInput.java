package com.ywh.rpc.common.entity;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息输入
 *
 * @author ywh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageInput {

	private String type;

	private String requestId;

	private String payload;

	public <T> T getPayload(Class<T> clazz) {
		if (payload == null) {
			return null;
		}
		return JSON.parseObject(payload, clazz);
	}

}
