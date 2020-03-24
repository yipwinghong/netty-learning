package com.ywh.im.protocol.response;


import com.ywh.im.protocol.BasePacket;

import static com.ywh.im.constant.Constant.HEARTBEAT_RESPONSE;

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
