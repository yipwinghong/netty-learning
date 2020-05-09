package com.ywh.im.common.protocol;

import com.ywh.im.common.session.Session;
import io.netty.util.AttributeKey;

/**
 * Channel 绑定属性
 * @author ywh
 */
public interface Attributes {
    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
