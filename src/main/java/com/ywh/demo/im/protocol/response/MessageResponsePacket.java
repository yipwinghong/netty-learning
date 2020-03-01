package com.ywh.demo.im.protocol.response;

import com.ywh.demo.im.protocol.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.ywh.demo.im.constant.CommandConstant.MESSAGE_RESPONSE;

/**
 * 消息响应协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponsePacket extends Packet {

    private String fromUserName;

    private String message;

    @Override
    public Byte getCommand() {

        return MESSAGE_RESPONSE;
    }
}
