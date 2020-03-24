package com.ywh.im.protocol.request;

import com.ywh.im.protocol.BasePacket;
import com.ywh.im.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;

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

        return Constant.LOGOUT_REQUEST;
    }
}
