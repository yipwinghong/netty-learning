package com.ywh.demo.im.protocol.response;


import com.ywh.demo.im.protocol.BasePacket;

import static com.ywh.demo.im.constant.Constant.HEARTBEAT_RESPONSE;

/**
 * 心跳响应协议包
 *
 * @author ywh
 */
public class HeartBeatResponsePacket extends BasePacket {
    @Override
    public Byte getCommand() {
        return HEARTBEAT_RESPONSE;
    }
}
