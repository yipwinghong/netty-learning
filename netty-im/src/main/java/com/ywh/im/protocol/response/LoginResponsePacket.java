package com.ywh.im.protocol.response;

import com.ywh.im.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.im.constant.Constant.LOGIN_RESPONSE;

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
