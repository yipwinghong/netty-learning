package com.ywh.im.common.protocol.request;

import com.ywh.im.common.protocol.BasePacket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.ywh.im.common.constant.Constant.MESSAGE_REQUEST;

/**
 * 消息请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestPacket extends BasePacket {

    private String toUserName;

    private String message;

    @Override
    public Byte getCommand() {
        return MESSAGE_REQUEST;
    }
}
