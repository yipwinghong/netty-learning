package com.ywh.im.common.protocol.response;

import com.ywh.im.common.protocol.BasePacket;
import com.ywh.im.common.session.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.im.common.constant.Constant.GROUP_MESSAGE_RESPONSE;

/**
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GroupMessageResponsePacket extends BasePacket {

    private String fromGroupName;

    private Session fromUser;

    private String message;

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_RESPONSE;
    }
}
