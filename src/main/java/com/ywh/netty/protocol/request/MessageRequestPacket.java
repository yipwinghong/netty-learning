package com.ywh.netty.protocol.request;

import com.ywh.netty.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.ywh.netty.constant.CommandConstant.MESSAGE_REQUEST;

/**
 * 消息请求协议包
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class MessageRequestPacket extends Packet {

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }

    public MessageRequestPacket(String message) {
        this.message = message;
    }
}
