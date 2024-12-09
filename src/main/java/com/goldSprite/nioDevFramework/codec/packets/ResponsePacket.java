package com.goldSprite.nioDevFramework.codec.packets;

import com.goldSprite.nioDevFramework.codec.codecInterfaces.Packet;

public abstract class ResponsePacket extends Packet {
    public ResponsePacket(int ownerGuid, int repCode) {
        super(ownerGuid);
        setCode(repCode);
    }
}
