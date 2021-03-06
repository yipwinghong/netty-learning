package com.ywh.im.server.handler;

import com.ywh.im.common.protocol.BasePacket;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;
import java.util.Map;

import static com.ywh.im.common.constant.Constant.*;

/**
 * 压缩 Handler（只取出指定命令对应的 Handler，不需要经过其他）
 * {@link ChannelHandler.Sharable} 标识一个 ChannelHandler 可以被多个 Channel 安全地共享。
 * 一个 ChannelHandler 可以从属于多个 ChannelPipeline，所以它也可以绑定到多个 ChannelHandlerContext实例。
 * 如果没有添加 Sharable 注解，当试图将它添加到多个 ChannelPipeline 时将会触发异常。
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
