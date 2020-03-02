package com.ywh.demo.rpc.common.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息输出
 * @author ywh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageOutput {

	private String requestId;

	private String type;

	private Object payload;

}
