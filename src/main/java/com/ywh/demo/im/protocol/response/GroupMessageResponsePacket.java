package com.ywh.demo.im.protocol.response;

import com.ywh.demo.im.protocol.Packet;
import com.ywh.demo.im.session.Session;
import lombok.Data;

import static com.ywh.demo.im.constant.CommandConstant.GROUP_MESSAGE_RESPONSE;

@Data
public class GroupMessageResponsePacket extends Packet {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {

        return GROUP_MESSAGE_RESPONSE;
    }
}
