package com.ywh.demo.im.protocol.response;

import com.ywh.demo.im.protocol.BasePacket;
import com.ywh.demo.im.session.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.demo.im.constant.CommandConstant.GROUP_MESSAGE_RESPONSE;

/**
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupMessageResponsePacket extends BasePacket {

    private String fromGroupId;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_RESPONSE;
    }
}
