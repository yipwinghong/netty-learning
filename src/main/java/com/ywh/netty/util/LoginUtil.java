package com.ywh.netty.util;

import com.ywh.netty.protocol.Attributes;
import io.netty.channel.Channel;

/**
 * @author ywh
 * @since 29/02/2020
 */
public class LoginUtil {

    /**
     * 标记登录状态
     *
     * @param channel
     */
    public static void markAsLogin(Channel channel) {
        channel.attr(Attributes.LOGIN).set(true);
    }

    /**
     * 判断登录状态
     *
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel) {
        return channel.attr(Attributes.LOGIN).get() != null;
    }
}
