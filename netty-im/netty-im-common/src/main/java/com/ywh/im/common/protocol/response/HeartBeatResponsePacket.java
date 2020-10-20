package com.ywh.im.common.protocol.response;


import com.ywh.im.common.protocol.BasePacket;

import static com.ywh.im.common.constant.Constant.HEARTBEAT_RESPONSE;

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
