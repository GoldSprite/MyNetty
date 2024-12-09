package com.goldSprite.nioDevFramework.codec.packets;


import com.goldSprite.nioDevFramework.codec.codecInterfaces.ICommand;

public class LoginResponsePacket extends ResponsePacket {

    public LoginResponsePacket(int ownerGuid, int repCode) {
        super(ownerGuid, repCode);
    }

    @Override
    public byte getCommand() {
        return ICommand.LOGIN_RESPONSE;
    }
}
