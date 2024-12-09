package com.goldSprite.nioDevFramework.other;


import com.goldSprite.nioDevFramework.codec.codecInterfaces.Packet;

public interface PacketCallback2<T extends Packet> {

    void callback(T pk);
}