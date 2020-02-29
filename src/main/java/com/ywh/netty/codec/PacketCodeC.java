package com.ywh.netty.codec;

import com.ywh.netty.protocol.Packet;
import com.ywh.netty.protocol.request.LoginRequestPacket;
import com.ywh.netty.protocol.request.MessageRequestPacket;
import com.ywh.netty.protocol.response.LoginResponsePacket;
import com.ywh.netty.protocol.response.MessageResponsePacket;
import com.ywh.netty.serializer.Serializer;
import com.ywh.netty.serializer.impl.JSONSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.HashMap;
import java.util.Map;

import static com.ywh.netty.constant.CommandConstant.*;

/**
 * 编解码器：
 * 通讯协议由六部分组成：
 * 1. 魔数：4bytes，用于限制解析指定的数据包协议
 * 2. 版本号：1bytes，预留字段，用于协议升级（类似 IPV4 和 IPV6）
 * 3. 序列化算法：1bytes
 * 4. 指令：1bytes
 * 5. 数据长度：4bytes
 * 6. 数据：Nbytes
 *
 * @author ywh
 */
public class PacketCodeC {

    private static final Map<Byte, Class<? extends Packet>> PACKET_TYPE_MAP;

    private static final Map<Byte, Serializer> SERIALIZER_MAP;

    public static final PacketCodeC INSTANCE = new PacketCodeC();

    static {
        PACKET_TYPE_MAP = new HashMap<>();
        PACKET_TYPE_MAP.put(LOGIN_REQUEST, LoginRequestPacket.class);
        PACKET_TYPE_MAP.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        PACKET_TYPE_MAP.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        PACKET_TYPE_MAP.put(MESSAGE_RESPONSE, MessageResponsePacket.class);

        SERIALIZER_MAP = new HashMap<>();
        Serializer serializer = new JSONSerializer();
        SERIALIZER_MAP.put(serializer.getSerializerAlgorithm(), serializer);
    }

    /**
     * 数据包编码
     *
     * @param packet
     * @return
     */
    public static ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {
        // 创建 ByteBuf 对象（创建一个直接内存，不受 JVM 堆管理，写到 IO 缓冲区效率更高）
        // ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        ByteBuf byteBuf = byteBufAllocator.buffer();

        // 序列化 java 对象
        byte[] bytes = Serializer.DEFAULT.serialize(packet);

        // 编码
        byteBuf.writeInt(MAGIC_NUMBER);
        byteBuf.writeByte(packet.getVersion());
        byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlgorithm());
        byteBuf.writeByte(packet.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    /**
     * 数据包解码
     *
     * @param byteBuf
     * @return
     */
    public static Packet decode(ByteBuf byteBuf) {
        // 跳过魔数和版本号，版本号
        byteBuf.skipBytes(4 + 1);

        // 读取序列化算法、指令和数据包长度
        byte serializeAlgorithm = byteBuf.readByte();
        byte command = byteBuf.readByte();
        int length = byteBuf.readInt();

        // 读取数据
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = PACKET_TYPE_MAP.get(command);
        Serializer serializer = SERIALIZER_MAP.get(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }
}
