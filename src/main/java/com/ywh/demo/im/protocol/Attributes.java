package com.ywh.demo.im.protocol;

import com.ywh.demo.im.session.Session;
import io.netty.util.AttributeKey;

/**
 * Channel 绑定属性
 * @author ywh
 */
public interface Attributes {

    AttributeKey<Session> SESSION = AttributeKey.newInstance("session");
}
