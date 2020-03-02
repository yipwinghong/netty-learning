package com.ywh.demo.im.protocol.request;

import com.ywh.demo.im.protocol.BasePacket;

import static com.ywh.demo.im.constant.CommandConstant.HEARTBEAT_REQUEST;

/**
 * 客户端心跳请求协议包
 *
 * @author ywh
 */
public class HeartBeatRequestPacket extends BasePacket {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_REQUEST;
    }
}
