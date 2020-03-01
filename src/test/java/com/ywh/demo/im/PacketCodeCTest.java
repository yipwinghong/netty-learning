package com.ywh.demo.im;

import com.ywh.demo.im.codec.PacketCodeC;
import com.ywh.demo.im.protocol.Packet;
import com.ywh.demo.im.protocol.request.LoginRequestPacket;
import com.ywh.demo.im.serializer.Serializer;
import com.ywh.demo.im.serializer.JSONSerializer;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试消息编解码
 *
 * @author ywh
 * @since 29/02/2020
 */
public class PacketCodeCTest {
    @Test
    public void encode() {

        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUserName("ywh");
        loginRequestPacket.setPassword("123456");
        ByteBuf byteBuf = PacketCodeC.encode(loginRequestPacket);
        Packet decodedPacket = PacketCodeC.decode(byteBuf);
        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));

    }
}
