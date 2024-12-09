package com.goldSprite.nioDevFramework.handlers;


import com.goldSprite.nioDevFramework.codec.PacketCodeC;
import com.goldSprite.nioDevFramework.codec.codecInterfaces.Packet;
import com.goldSprite.nioDevFramework.other.ClientInfoStatus;
import com.goldSprite.nioDevFramework.starter.Server;
import com.goldSprite.nioDevFramework.tools.LogTools;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

//import lombok.var;

public class PacketEncoder extends ChannelOutboundHandlerAdapter {
    private final boolean isServer;

    public PacketEncoder() {
        this.isServer = false;
    }

    public PacketEncoder(boolean isServer) {
        this.isServer = isServer;
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
//        LogTools.NLog(ctx.name()+": CustomPacketEncoderHandler.flush");
        ctx.flush();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//        LogTools.NLog(ctx.name()+": CustomPacketEncoderHandler.write");
        try{
            DatagramPacket dpk = encodeAuthentication((Packet) msg, ctx);
            if(dpk != null)
                ctx.write(dpk);
        }catch (Exception e){
            exceptionCaught(e);
        }
    }

    //将Packet包裹为DatagramPacket
    private DatagramPacket encodeAuthentication(Packet pk, ChannelHandlerContext ctx) {
        var info = "发包验证异常: ";
        InetSocketAddress networkAddress = Server.networkAddress;
        if(isServer){
            if(!Server.Instance.isOnline(pk.getOwnerGuid())){
                LogTools.NLogInfo(info+"该玩家不在线上.");
                return null;
            }
            ClientInfoStatus client = Server.Instance.clients.get(pk.getOwnerGuid());
            networkAddress = client.address;
            LogTools.NLogDebug("目标玩家地址: "+networkAddress);
        }
        if(networkAddress == null){
            LogTools.NLogInfo(info+"发送地址为空.");
            return null;
        }
        DatagramPacket dpk = PacketCodeC.INSTANCE.encodeDpk(ctx.alloc(), pk, networkAddress);
        return dpk;
    }

    public void exceptionCaught(Exception e) throws Exception {
        LogTools.NLogErr("处理器异常: ");
        e.printStackTrace();
    }

}
