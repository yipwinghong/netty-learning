package com.ywh.demo.im.protocol.request;

import com.ywh.demo.im.protocol.BasePacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.ywh.demo.im.constant.CommandConstant.LOGOUT_REQUEST;

/**
 * 登出请求协议包
 *
 * @author ywh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LogoutRequestPacket extends BasePacket {
    @Override
    public Byte getCommand() {

        return LOGOUT_REQUEST;
    }
}
