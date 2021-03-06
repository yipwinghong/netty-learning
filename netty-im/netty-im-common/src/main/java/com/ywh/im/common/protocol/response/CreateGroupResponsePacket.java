package com.ywh.im.common.protocol.response;

import com.ywh.im.common.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import static com.ywh.im.common.constant.Constant.CREATE_GROUP_RESPONSE;

/**
 * 创建群组响应协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateGroupResponsePacket extends BasePacket {
    private boolean success;

    private String groupName;

    private List<String> userNameList;

    @Override
    public Byte getCommand() {

        return CREATE_GROUP_RESPONSE;
    }
}
