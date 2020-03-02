package com.ywh.demo.im.server.handler;

import com.ywh.demo.im.protocol.BasePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

import static com.ywh.demo.im.constant.CommandConstant.*;

/**
 * 压缩 Handler（只取出指定命令对应的 Handler，不需要经过其他）
 *
 * @author ywh
 */
@ChannelHandler.Sharable
public class ImHandler extends SimpleChannelInboundHandler<BasePacket> {

    public static final ImHandler INSTANCE = new ImHandler();

    private Map<Byte, SimpleChannelInboundHandler<? extends BasePacket>> handlerMap;

    private ImHandler() {
        handlerMap = new HashMap<>();

        // 单聊消息请求处理器
        handlerMap.put(MESSAGE_REQUEST, MessageRequestHandler.INSTANCE);

        // 创建群请求处理器
        handlerMap.put(CREATE_GROUP_REQUEST, CreateGroupRequestHandler.INSTANCE);

        // 加群请求处理器
        handlerMap.put(JOIN_GROUP_REQUEST, JoinGroupRequestHandler.INSTANCE);

        // 退群请求处理器
        handlerMap.put(QUIT_GROUP_REQUEST, QuitGroupRequestHandler.INSTANCE);

        // 退群请求处理器
        handlerMap.put(LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestHandler.INSTANCE);

        // 获取群成员请求处理器
        handlerMap.put(GROUP_MESSAGE_REQUEST, GroupMessageRequestHandler.INSTANCE);

        // 登出请求处理器
        handlerMap.put(LOGOUT_REQUEST, LogoutRequestHandler.INSTANCE);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, BasePacket packet) throws Exception {
        handlerMap.get(packet.getCommand()).channelRead(ctx, packet);
    }
}
