package com.ywh.demo.im.protocol.request;

import com.ywh.demo.im.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.demo.im.constant.Constant.LIST_GROUP_MEMBERS_REQUEST;

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

        return LIST_GROUP_MEMBERS_REQUEST;
    }
}
