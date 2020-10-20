package com.ywh.im.common.protocol.serializer;


/**
 * 序列化器
 *
 * @author ywh
 */
public interface Serializer {

    Serializer DEFAULT = new JsonSerializer();

    /**
     * 序列化算法
     * @return
     */
    byte getSerializerAlgorithm();

    /**
     * 序列化：Java 对象转换成二进制
     *
     * @param object
     * @return
     */
    byte[] serialize(Object object);

    /**
     * 反序列化：二进制转换成 Java 对象
     *
     * @param clazz
     * @param bytes
     * @param <T>
     * @return
     */
    <T> T deserialize(Class<T> clazz, byte[] bytes);

}
