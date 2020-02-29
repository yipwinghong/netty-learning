package com.ywh.netty.protocol;

import io.netty.util.AttributeKey;

/**
 * Channel 绑定属性
 * @author ywh
 */
public interface Attributes {
    AttributeKey<Boolean> LOGIN = AttributeKey.newInstance("login");
}
