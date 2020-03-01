package com.ywh.demo.im.protocol.request;

import com.ywh.demo.im.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.demo.im.constant.CommandConstant.JOIN_GROUP_REQUEST;

/**
 * 加入群组请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JoinGroupRequestPacket extends Packet {

    private String groupName;

    @Override
    public Byte getCommand() {

        return JOIN_GROUP_REQUEST;
    }
}
