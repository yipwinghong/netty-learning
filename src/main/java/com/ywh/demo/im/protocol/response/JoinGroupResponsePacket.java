package com.ywh.demo.im.protocol.response;

import com.ywh.demo.im.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.demo.im.constant.CommandConstant.JOIN_GROUP_RESPONSE;

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
