package com.ywh.netty.constant;

/**
 * 命令常量
 *
 * @author ywh
 */
public interface CommandConstant {

    Byte LOGIN_REQUEST = 1;

    Byte LOGIN_RESPONSE = 2;

    Byte MESSAGE_REQUEST = 3;

    Byte MESSAGE_RESPONSE = 4;

    Integer MAGIC_NUMBER = 0x12345678;
}
