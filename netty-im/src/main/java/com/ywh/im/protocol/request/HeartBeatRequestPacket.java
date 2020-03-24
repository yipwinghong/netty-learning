package com.ywh.im.protocol.request;

import com.ywh.im.protocol.BasePacket;
import com.ywh.im.constant.Constant;

/**
 * 客户端心跳请求协议包
 *
 * @author ywh
 */
public class HeartBeatRequestPacket extends BasePacket {
    @Override
    public Byte getCommand() {
        return Constant.HEARTBEAT_REQUEST;
    }
}
