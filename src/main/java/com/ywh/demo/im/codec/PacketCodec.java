package com.ywh.demo.im.codec;

import com.ywh.demo.im.protocol.BasePacket;
import com.ywh.demo.im.protocol.request.*;
import com.ywh.demo.im.protocol.response.*;
import com.ywh.demo.im.serializer.Serializer;
import com.ywh.demo.im.serializer.JsonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.HashMap;
import java.util.Map;

import static com.ywh.demo.im.constant.Constant.*;


/**
 * 编解码器：
 * 通讯协议由六部分组成：
 * 1. 魔数：4bytes，用于限制解析指定的数据包协议
 * 2. 版本号：1bytes，预留字段，用于协议升级（类似 IPV4 和 IPV6）
 * 3. 序列化算法：1bytes
 * 4. 指令：1bytes
 * 5. 数据长度：4bytes，便于拆包：使用 {@link LengthFieldBasedFrameDecoder}（基于长度域拆包器），长度域相对整个数据包的偏移量为 4 + 1 + 1 + 1 == 7
 * 6. 数据：Nbytes
 *
 *
 * @author ywh
 */
public class PacketCodec {

    public static final PacketCodec INSTANCE = new PacketCodec();

    private final Map<Byte, Class<? extends BasePacket>> packetTypeMap;
    private final Map<Byte, Serializer> serializerMap;


    private PacketCodec() {
        packetTypeMap = new HashMap<>();
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(LOGOUT_REQUEST, LogoutRequestPacket.class);
        packetTypeMap.put(LOGOUT_RESPONSE, LogoutResponsePacket.class);
        packetTypeMap.put(CREATE_GROUP_REQUEST, CreateGroupRequestPacket.class);
        packetTypeMap.put(CREATE_GROUP_RESPONSE, CreateGroupResponsePacket.class);
        packetTypeMap.put(JOIN_GROUP_REQUEST, JoinGroupRequestPacket.class);
        packetTypeMap.put(JOIN_GROUP_RESPONSE, JoinGroupResponsePacket.class);
        packetTypeMap.put(QUIT_GROUP_REQUEST, QuitGroupRequestPacket.class);
        packetTypeMap.put(QUIT_GROUP_RESPONSE, QuitGroupResponsePacket.class);
        packetTypeMap.put(LIST_GROUP_MEMBERS_REQUEST, ListGroupMembersRequestPacket.class);
        packetTypeMap.put(LIST_GROUP_MEMBERS_RESPONSE, ListGroupMembersResponsePacket.class);
        packetTypeMap.put(GROUP_MESSAGE_REQUEST, GroupMessageRequestPacket.class);
        packetTypeMap.put(GROUP_MESSAGE_RESPONSE, GroupMessageResponsePacket.class);
        packetTypeMap.put(HEARTBEAT_REQUEST, HeartBeatRequestPacket.class);
        packetTypeMap.put(HEARTBEAT_RESPONSE, HeartBeatResponsePacket.class);

        serializerMap = new HashMap<>();
        Serializer serializer = new JsonSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);
    }

    public ByteBuf encode(BasePacket packet) {
        // 创建 ByteBuf 对象（创建一个直接内存，不受 JVM 堆管理，写到 IO 缓冲区效率更高）
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        encode(byteBuf, packet);
        return byteBuf;
    }

    public void encode(ByteBuf byteBuf, BasePacket packet) {
        // 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 编码
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }


    public BasePacket decode(ByteBuf byteBuf) {
        // 跳过 magic number
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
