package com.ywh.im.common.protocol.response;

import com.ywh.im.common.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.im.common.constant.Constant.JOIN_GROUP_RESPONSE;

/**
 * 加入群组响应协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JoinGroupResponsePacket extends BasePacket {
    private String groupName;

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {

        return JOIN_GROUP_RESPONSE;
    }
}
