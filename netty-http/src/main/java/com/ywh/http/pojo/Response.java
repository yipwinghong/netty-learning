package com.ywh.http.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author ywh
 * @since 2020/3/24/024
 */
@Data
public class Response {

    private Integer code;

    private Object data;

    private String method;

    private Date timestamp;
}
