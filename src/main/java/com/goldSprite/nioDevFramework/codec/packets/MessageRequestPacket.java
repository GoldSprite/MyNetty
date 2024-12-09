package com.goldSprite.nioDevFramework.codec.packets;

import com.goldSprite.nioDevFramework.codec.codecInterfaces.ICommand;
import com.goldSprite.nioDevFramework.codec.codecInterfaces.Packet;
import lombok.*;

public class MessageRequestPacket extends Packet {
    @Getter
    private final String message;

    public MessageRequestPacket(int ownerGuid, String message) {
        super(ownerGuid);
        this.message = message;
    }

    @Override
    public byte getCommand() {
        return ICommand.MESSAGE_REQUEST;
    }
}
