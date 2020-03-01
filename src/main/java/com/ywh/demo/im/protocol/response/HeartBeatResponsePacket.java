package com.ywh.demo.im.protocol.response;


import com.ywh.demo.im.protocol.Packet;

import static com.ywh.demo.im.constant.CommandConstant.HEARTBEAT_RESPONSE;

/**
 * 心跳响应协议包
 *
 * @author ywh
 */
public class HeartBeatResponsePacket extends Packet {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }
}
