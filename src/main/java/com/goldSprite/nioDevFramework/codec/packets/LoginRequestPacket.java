package com.goldSprite.nioDevFramework.codec.packets;

import com.goldSprite.nioDevFramework.codec.codecInterfaces.ICommand;
import com.goldSprite.nioDevFramework.codec.codecInterfaces.Packet;
import lombok.Getter;

public class LoginRequestPacket extends Packet {
    @Getter
    private final String userName;
    @Getter
    private final String password;

    public LoginRequestPacket(int ownerGuid, String userName, String password) {
        super(ownerGuid);
        this.userName = userName;
        this.password = password;
    }

    @Override
    public byte getCommand() {
        return ICommand.LOGIN_REQUEST;
    }

}
