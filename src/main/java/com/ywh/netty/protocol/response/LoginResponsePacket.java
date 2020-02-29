package com.ywh.netty.protocol.response;

import com.ywh.netty.protocol.Packet;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.netty.constant.CommandConstant.LOGIN_RESPONSE;

/**
 * 登录响应协议包
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginResponsePacket extends Packet {
    private boolean success;

    private String reason;

    @Override
    public Byte getCommand() {
        return LOGIN_RESPONSE;
    }
}
