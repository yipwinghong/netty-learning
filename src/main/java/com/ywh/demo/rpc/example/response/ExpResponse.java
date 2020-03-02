package com.ywh.demo.rpc.example.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 指数响应
 *
 * @author ywh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpResponse {

	private long value;
	private long costInNanos;
}
