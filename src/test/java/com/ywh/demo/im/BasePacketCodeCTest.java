package com.ywh.demo.im;

import com.ywh.demo.im.protocol.BasePacket;
import com.ywh.demo.im.protocol.PacketCodec;
import com.ywh.demo.im.protocol.request.LoginRequestPacket;
import com.ywh.demo.im.protocol.serializer.Serializer;
import com.ywh.demo.im.protocol.serializer.JsonSerializer;
import io.netty.buffer.ByteBuf;
import org.junit.Assert;
import org.junit.Test;

/**
 * 测试消息编解码
 *
 * @author ywh
 * @since 29/02/2020
 */
public class BasePacketCodeCTest {
    @Test
    public void encode() {

        Serializer serializer = new JsonSerializer();
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();

        loginRequestPacket.setVersion(((byte) 1));
        loginRequestPacket.setUserName("ywh");
        loginRequestPacket.setPassword("123456");

        ByteBuf byteBuf = PacketCodec.INSTANCE.encode(loginRequestPacket);
        BasePacket decodedPacket = PacketCodec.INSTANCE.decode(byteBuf);
        Assert.assertArrayEquals(serializer.serialize(loginRequestPacket), serializer.serialize(decodedPacket));

    }
}
