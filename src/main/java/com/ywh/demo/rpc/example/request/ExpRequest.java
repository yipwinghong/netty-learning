package com.ywh.demo.rpc.example.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指数请求
 *
 * @author ywh
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpRequest {
	private int base;
	private int exp;
}
