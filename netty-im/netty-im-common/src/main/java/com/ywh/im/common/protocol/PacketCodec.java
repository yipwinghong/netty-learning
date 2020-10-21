package com.ywh.im.common.protocol;

import com.ywh.im.common.constant.*;
import com.ywh.im.common.handler.CustomizedFrameDecoder;
import com.ywh.im.common.protocol.request.*;
import com.ywh.im.common.protocol.response.*;
import com.ywh.im.common.protocol.serializer.JsonSerializer;
import com.ywh.im.common.protocol.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.HashMap;
import java.util.Map;


/**
 * 编解码器，通讯协议由六部分组成：
 *              +-----------------------------------> 魔数：4bytes，用于限制解析指定的数据包协议
 *              |            +----------------------> 版本号：1bytes，预留字段，用于协议升级（类似 IPV4 和 IPV6）
 *      0--------------3---------4------------5
 *      | Magic Number | Version | Serializer | ----> 序列化算法：1bytes
 *      +---------+----+---+-----+------------+
 *      | Command | Length |       Data       | ----> 数据：N bytes
 *      5---------6--------10-----------------n
 *           |         +----------------------------> 数据长度：4bytes，便于拆包。
 *           +--------------------------------------> 指令：1bytes
 * 由于使用 {@link LengthFieldBasedFrameDecoder}（基于长度域拆包器），长度域相对整个数据包的偏移量为 4 + 1 + 1 + 1 == 7，见 {@link CustomizedFrameDecoder}
 * @author ywh
 */
public class PacketCodec {

    public static final PacketCodec INSTANCE = new PacketCodec();

    private final Map<Byte, Class<? extends BasePacket>> packetTypeMap;

    private final Map<Byte, Serializer> serializerMap;

    private PacketCodec() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(Constant.LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(Constant.LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(Constant.MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(Constant.MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(Constant.LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetTypeMap.put(Constant.LOGOUT_RESPONSE, LogoutResponsePacket.class);
        packetTypeMap.put(Constant.CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(Constant.CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        packetTypeMap.put(Constant.JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        packetTypeMap.put(Constant.JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        packetTypeMap.put(Constant.QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
        packetTypeMap.put(Constant.QUIT_GROUP_RESPONSE, QuitGroupResponsePacket.class);
        packetTypeMap.put(Constant.LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestPacket.class);
        packetTypeMap.put(Constant.LIST_GROUP_MEMBERS_RESPONSE, ListGroupMembersResponsePacket.class);
        packetTypeMap.put(Constant.GROUP_MESSAGE_REQUEST, GroupMessageRequestPacket.class);
        packetTypeMap.put(Constant.GROUP_MESSAGE_RESPONSE, GroupMessageResponsePacket.class);
        packetTypeMap.put(Constant.HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        packetTypeMap.put(Constant.HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JsonSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public void encode(ByteBuf byteBuf, BasePacket packet) {
        // 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 编码
        byteBuf.writeInt(Constant.MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }


    public BasePacket decode(ByteBuf byteBuf) {
        // 跳过魔数
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends BasePacket> requestType = getRequestType(command);
        Serializer serializer = getSerializer(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    private Serializer getSerializer(byte serializeAlgorithm) {
        return serializerMap.get(serializeAlgorithm);
    }

    private Class<? extends BasePacket> getRequestType(byte command) {
        return packetTypeMap.get(command);
    }
}
