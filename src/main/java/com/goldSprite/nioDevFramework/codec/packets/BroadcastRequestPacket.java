package com.goldSprite.nioDevFramework.codec.packets;

import com.goldSprite.nioDevFramework.codec.codecInterfaces.ICommand;
import com.goldSprite.nioDevFramework.codec.codecInterfaces.Packet;
import lombok.Getter;

//通知就行, 不用管到不到位
public class BroadcastRequestPacket extends Packet {
    @Getter
    private final String message;

    public BroadcastRequestPacket(int ownerGuid, String message) {
        super(ownerGuid);
        this.message = message;
    }

    @Override
    public byte getCommand() {
        return ICommand.BROADCAST_REQUEST;
    }
}
