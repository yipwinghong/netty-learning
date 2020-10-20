package com.ywh.im.common.protocol.request;

import com.ywh.im.common.protocol.BasePacket;
import com.ywh.im.common.constant.Constant;

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
