package com.goldSprite.nioDevFramework.codec.packets;

import com.goldSprite.nioDevFramework.codec.codecInterfaces.ICommand;

public class MessageResponsePacket extends ResponsePacket {

    public MessageResponsePacket(int ownerGuid, int repCode) {
        super(ownerGuid, repCode);
    }

    @Override
    public byte getCommand() {
        return ICommand.MESSAGE_RESPONSE;
    }
}