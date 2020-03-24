package com.ywh.im.protocol.request;

import com.ywh.im.protocol.BasePacket;
import com.ywh.im.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 创建群组请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CreateGroupRequestPacket extends BasePacket {

    private List<String> userNameList;

    @Override
    public Byte getCommand() {
        return Constant.CREATE_GROUP_REQUEST;
    }
}
