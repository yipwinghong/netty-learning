package com.ywh.demo.im.protocol.request;

import com.ywh.demo.im.protocol.BasePacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.ywh.demo.im.constant.Constant.GROUP_MESSAGE_REQUEST;

/**
 * 群组消息请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupMessageRequestPacket extends BasePacket {
    private String toGroupId;
    private String message;

    @Override
    public Byte getCommand() {
        return GROUP_MESSAGE_REQUEST;
    }
}
