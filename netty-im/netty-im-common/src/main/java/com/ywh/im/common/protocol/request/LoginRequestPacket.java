package com.ywh.im.common.protocol.request;

import com.ywh.im.common.protocol.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.im.common.constant.Constant.LOGIN_REQUEST;

/**
 * 登录请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRequestPacket extends BasePacket {

    private String userName;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
