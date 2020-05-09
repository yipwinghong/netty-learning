package com.ywh.im.common.protocol.request;

import com.ywh.im.common.protocol.BasePacket;
import com.ywh.im.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查看群组成员请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListGroupMembersRequestPacket extends BasePacket {

    private String groupName;

    @Override
    public Byte getCommand() {

        return Constant.LIST_GROUP_MEMBERS_REQUEST;
    }
}
