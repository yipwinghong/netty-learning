package com.ywh.im.common.protocol.request;

import com.ywh.im.common.protocol.BasePacket;
import com.ywh.im.common.constant.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    private String toGroupName;

    private String message;

    @Override
    public Byte getCommand() {
        return Constant.GROUP_MESSAGE_REQUEST;
    }
}
