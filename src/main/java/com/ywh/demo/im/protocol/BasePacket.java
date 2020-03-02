package com.ywh.demo.im.protocol;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 通信协议包
 * @author ywh
 */
@Data
public abstract class BasePacket {

    /**
     * 版本
     */
    @JSONField(deserialize = false, serialize = false)
    private Byte version = 1;

    /**
     * 获取命令
     *
     * @return
     */
    @JSONField(serialize = false)
    public abstract Byte getCommand();
}
