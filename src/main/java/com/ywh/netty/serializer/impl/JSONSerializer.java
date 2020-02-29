package com.ywh.netty.serializer.impl;

import com.alibaba.fastjson.JSON;
import com.ywh.netty.serializer.Serializer;
import com.ywh.netty.serializer.SerializerAlgorithm;

/**
 * JSON 序列化器
 *
 * @author ywh
 */
public class JSONSerializer implements Serializer {

    @Override
    public byte getSerializerAlgorithm() {
        return SerializerAlgorithm.JSON;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
