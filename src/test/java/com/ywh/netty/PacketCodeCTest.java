package com.ywh.netty;

import com.ywh.netty.protocol.request.LoginRequestPacket;
import com.ywh.netty.serializer.Serializer;
import com.ywh.netty.serializer.impl.JSONSerializer;
import org.junit.Test;

/**
 * @author ywh
 * @since 29/02/2020
 */
public class PacketCodeCTest {
    @Test
    public void encode() {

        Serializer serializer = new JSONSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUserId("123");
        loginRequestPacket.setUsername("zhangsan");
        loginRequestPacket.setPassword("password");

//        PacketCodeC packetCodeC = new PacketCodeC();
//        ByteBuf byteBuf = PacketCodeC.encode(loginRequestPacket);
//        Packet decodedPacket = packetCodeC.decode(byteBuf);
//
//        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));

    }
}
