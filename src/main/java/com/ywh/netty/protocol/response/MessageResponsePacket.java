package com.ywh.netty.protocol.response;

import com.ywh.netty.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.netty.constant.CommandConstant.MESSAGE_RESPONSE;

/**
 * 消息响应协议包
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MessageResponsePacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {

        return MESSAGE_RESPONSE;
    }
}
