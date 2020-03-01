package com.ywh.demo.im.protocol.request;

import com.ywh.demo.im.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.ywh.demo.im.constant.CommandConstant.GROUP_MESSAGE_REQUEST;

/**
 * 群组消息请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class GroupMessageRequestPacket extends Packet {
    private String toGroupId;
    private String message;

    public GroupMessageRequestPacket(String toGroupId, String message) {
        this.toGroupId = toGroupId;
        this.message = message;
    }

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_REQUEST;
    }
}
