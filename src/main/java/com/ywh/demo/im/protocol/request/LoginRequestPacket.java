package com.ywh.demo.im.protocol.request;

import com.ywh.demo.im.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.demo.im.constant.CommandConstant.LOGIN_REQUEST;

/**
 * 登录请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRequestPacket extends Packet {

    private String userName;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
