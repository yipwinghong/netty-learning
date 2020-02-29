package com.ywh.netty.protocol.request;

import com.ywh.netty.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.netty.constant.CommandConstant.LOGIN_REQUEST;

/**
 * 登录请求协议包
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginRequestPacket extends Packet {
    private String userId;

    private String username;

    private String password;

    @Override
    public Byte getCommand() {
        return LOGIN_REQUEST;
    }
}
