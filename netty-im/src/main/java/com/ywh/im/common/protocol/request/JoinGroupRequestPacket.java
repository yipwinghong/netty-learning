package com.ywh.im.common.protocol.request;

import com.ywh.im.common.protocol.BasePacket;
import com.ywh.im.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 加入群组请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JoinGroupRequestPacket extends BasePacket {

    private String groupName;

    @Override
    public Byte getCommand() {

        return Constant.JOIN_GROUP_REQUEST;
    }
}
