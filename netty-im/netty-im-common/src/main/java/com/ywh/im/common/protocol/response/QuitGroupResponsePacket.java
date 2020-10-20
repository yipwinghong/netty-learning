package com.ywh.im.common.protocol.response;

import com.ywh.im.common.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.im.common.constant.Constant.QUIT_GROUP_RESPONSE;

/**
 * 退出群组响应协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuitGroupResponsePacket extends BasePacket {

    private String groupName;

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {

        return QUIT_GROUP_RESPONSE;
    }
}
