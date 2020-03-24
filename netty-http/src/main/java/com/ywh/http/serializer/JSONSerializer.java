package com.ywh.http.serializer;

import com.alibaba.fastjson.JSON;

/**
 * @author ywh
 * @since 2020/3/24/024
 */
public class JSONSerializer implements Serializer {
    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}