package com.goldSprite.nioDevFramework.handlers;

import com.goldSprite.nioDevFramework.tools.LogTools;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.DatagramPacket;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.InetSocketAddress;

//import lombok.var;

@Data
@EqualsAndHashCode(callSuper=false)
public class PacketDecoder extends ChannelInboundHandlerAdapter {
    private boolean isServer;
    private InetSocketAddress sender;

    public PacketDecoder() {
    }

    public PacketDecoder(boolean isServer) {
        this.isServer = isServer;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        LogTools.NLog(ctx.name() + ": CustomPacketDecoderHandler.channelReadComplete");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        LogTools.NLog(ctx.name() + ": CustomPacketDecoderHandler.channelRead");

        if (!(msg instanceof DatagramPacket)) throw new Exception("数据包格式异常.");
        DatagramPacket dpk = (DatagramPacket) msg;
        //这里直接解码下面拿不到sender
//        Packet pk = PacketCodeC.INSTANCE.decode(dpk.content());
//        if (pk == null) throw new Exception("数据包格式异常.");
        ctx.fireChannelRead(dpk);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogTools.NLogErr("处理器异常: ");
        cause.printStackTrace();
    }
}
