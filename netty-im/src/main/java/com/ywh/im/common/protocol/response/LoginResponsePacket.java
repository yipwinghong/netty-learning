package com.ywh.im.common.protocol.response;

import com.ywh.im.common.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.im.common.constant.Constant.LOGIN_RESPONSE;

/**
 * 登录响应协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponsePacket extends BasePacket {

    private String userName;

    private Boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
