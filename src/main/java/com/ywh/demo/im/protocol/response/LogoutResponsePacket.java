package com.ywh.demo.im.protocol.response;

import com.ywh.demo.im.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.demo.im.constant.Constant.LOGOUT_RESPONSE;

/**
 * 登出响应协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogoutResponsePacket extends BasePacket {

    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {

        return LOGOUT_RESPONSE;
    }
}
