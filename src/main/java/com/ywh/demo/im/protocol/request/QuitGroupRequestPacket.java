package com.ywh.demo.im.protocol.request;

import com.ywh.demo.im.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.demo.im.constant.CommandConstant.QUIT_GROUP_REQUEST;

/**
 * 退出群组请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuitGroupRequestPacket extends Packet {

    private String groupName;

    @Override
    public Byte getCommand() {
        return QUIT_GROUP_REQUEST;
    }
}
