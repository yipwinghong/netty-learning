package com.ywh.im.protocol.response;

import com.ywh.im.protocol.BasePacket;
import com.ywh.im.session.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.im.constant.Constant.GROUP_MESSAGE_RESPONSE;

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
