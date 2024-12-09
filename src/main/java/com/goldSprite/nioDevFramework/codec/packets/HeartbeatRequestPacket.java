package com.goldSprite.nioDevFramework.codec.packets;

import com.goldSprite.nioDevFramework.codec.codecInterfaces.Packet;

public class HeartbeatRequestPacket extends Packet {
    public HeartbeatRequestPacket(int ownerGuid) {
        super(ownerGuid);
    }

    @Override
    public byte getCommand() {
        return 0;
    }
}
