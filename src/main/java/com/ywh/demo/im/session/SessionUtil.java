package com.ywh.demo.im.session;

import com.ywh.demo.im.protocol.Attributes;
import com.ywh.demo.im.session.Session;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Session 工具类
 *
 * @author ywh
 */
public class SessionUtil {

    private static final Map<String, ChannelGroup> GROUP_ID_CHANNEL_GROUP_MAP = new ConcurrentHashMap<>();

    private static final Map<String, Channel> USER_ID_CHANNEL_MAP = new ConcurrentHashMap<>();

    /**
     * 绑定 Session，添加 Session 与 Channel 映射关系到缓存
     *
     * @param session
     * @param channel
     */
    public static void bindSession(Session session, Channel channel) {
        USER_ID_CHANNEL_MAP.put(session.getUserName(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    /**
     * 取消绑定 Session，从缓存移除 Session 与 Channel 映射关系
     *
     * @param channel
     */
    public static void unBindSession(Channel channel) {
        if (hasLogin(channel)) {
            USER_ID_CHANNEL_MAP.remove(getSession(channel).getUserName());
            channel.attr(Attributes.SESSION).set(null);
        }
    }

    /**
     * 判断用户是否已登录
     *
     * @param channel
     * @return
     */
    public static boolean hasLogin(Channel channel) {
        return channel != null && channel.hasAttr(Attributes.SESSION);
    }

    /**
     * 根据 Channel 获取 Session
     *
     * @param channel
     * @return
     */
    public static Session getSession(Channel channel) {
        return channel.attr(Attributes.SESSION).get();
    }

    /**
     * 根据 userName 获取 Channel
     *
     * @return
     */
    public static Channel getChannel(String userName) {
        return USER_ID_CHANNEL_MAP.get(userName);
    }

    /**
     * 绑定群组
     *
     * @param groupName
     * @param channelGroup
     */
    public static void bindChannelGroup(String groupName, ChannelGroup channelGroup) {
        GROUP_ID_CHANNEL_GROUP_MAP.put(groupName, channelGroup);
    }

    /**
     * 根据名称获取群组
     *
     * @param groupName
     * @return
     */
    public static ChannelGroup getChannelGroup(String groupName) {
        return GROUP_ID_CHANNEL_GROUP_MAP.get(groupName);
    }
}
