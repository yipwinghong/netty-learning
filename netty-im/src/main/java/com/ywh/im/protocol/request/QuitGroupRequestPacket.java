package com.ywh.im.protocol.request;

import com.ywh.im.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.im.constant.Constant.QUIT_GROUP_REQUEST;

/**
 * 退出群组请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuitGroupRequestPacket extends BasePacket {

    private String groupName;

    @Override
    public Byte getCommand() {
        return QUIT_GROUP_REQUEST;
    }
}
