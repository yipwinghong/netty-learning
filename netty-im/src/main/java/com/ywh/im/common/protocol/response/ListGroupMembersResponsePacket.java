package com.ywh.im.common.protocol.response;

import com.ywh.im.common.protocol.BasePacket;
import com.ywh.im.common.session.Session;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import static com.ywh.im.common.constant.Constant.LIST_GROUP_MEMBERS_RESPONSE;

/**
 * 查看群组成员响应协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ListGroupMembersResponsePacket extends BasePacket {

    private String groupName;

    private List<Session> sessionList;

    @Override
    public Byte getCommand() {

        return LIST_GROUP_MEMBERS_RESPONSE;
    }
}
