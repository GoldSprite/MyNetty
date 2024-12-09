package com.goldSprite.nioDevFramework.codec.packets;

import com.goldSprite.nioDevFramework.codec.codecInterfaces.ICommand;
import lombok.Getter;

public class BroadcastResponsePacket extends ResponsePacket {
    @Getter
    private final String message;

    public BroadcastResponsePacket(int ownerGuid, int repCode, String message) {
        super(ownerGuid, repCode);
        this.message = message;
    }

    @Override
    public byte getCommand() {
        return ICommand.BROADCAST_RESPONSE;
    }
}
