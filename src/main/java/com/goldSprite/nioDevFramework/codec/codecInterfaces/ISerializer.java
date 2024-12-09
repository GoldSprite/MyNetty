package com.goldSprite.nioDevFramework.codec.codecInterfaces;

import com.goldSprite.nioDevFramework.codec.serializers.JSONSerializer;

public interface ISerializer {
    ISerializer DEFAULT = new JSONSerializer();

    byte getSerializerAlgorithm();
    byte[] serialize(Object object);
    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
