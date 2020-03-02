package com.ywh.demo.im.protocol.request;

import com.ywh.demo.im.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

import static com.ywh.demo.im.constant.CommandConstant.CREATE_GROUP_REQUEST;

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
        return CREATE_GROUP_REQUEST;
    }
}
