package com.ywh.im.common.protocol.response;

import com.ywh.im.common.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.im.common.constant.Constant.LOGOUT_RESPONSE;

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
