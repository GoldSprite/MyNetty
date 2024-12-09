package com.goldSprite.nioDevFramework.handlers;

import com.goldSprite.nioDevFramework.codec.PacketCodeC;
import com.goldSprite.nioDevFramework.codec.codecInterfaces.IStatus;
import com.goldSprite.nioDevFramework.codec.packets.*;
import com.goldSprite.nioDevFramework.other.PacketCallback2;
import com.goldSprite.nioDevFramework.codec.codecInterfaces.ICommand;
import com.goldSprite.nioDevFramework.codec.codecInterfaces.Packet;
import com.goldSprite.nioDevFramework.starter.Client;
import com.goldSprite.nioDevFramework.starter.Server;
import com.goldSprite.nioDevFramework.tools.LogTools;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//import lombok.var;

public class PacketsHandler extends SimpleChannelInboundHandler<DatagramPacket> {
    private boolean isServer;
    private final HashMap<Class<? extends Packet>, List<PacketCallback2<Packet>>> callbacks = new HashMap<>();

    public PacketsHandler() {
    }

    public PacketsHandler(boolean isServer) {
        this.isServer = isServer;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        LogTools.NLog("频道激活.");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        LogTools.NLog(ctx.name() + ": CustomPacketHandler.channelReadComplete");
        ctx.channel().flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket dpk) throws Exception {
//        LogTools.NLog(ctx.name() + ": CustomPacketHandler.channelRead");
        var sender = dpk.sender();
        var packet = PacketCodeC.INSTANCE.decode(dpk.content());

        LogTools.NLogDebug("收到包类型: " + packet.getClass().getSimpleName());
        if (Server.strangerIntercept)
            if (isServer) {
                var intercept = strangerInterceptor(packet);
                if (intercept) {
                    LogTools.NLogInfo("已拦截未登录者消息.");
                    var rep = new LoginResponsePacket(packet.getOwnerGuid(), IStatus.RETURN_DEFEAT_LOGIN_NOTLOGIN);
                    Server.Instance.sendPacket(rep);
                    return;
                }
            }

        switch (packet.getCommand()) {
            case ICommand.LOGIN_REQUEST: {
                LoginRequestPacket pk = (LoginRequestPacket) packet;
                handleLoginRequestPacket(pk, sender);
                break;
            }
            case ICommand.LOGIN_RESPONSE: {
                LoginResponsePacket pk = (LoginResponsePacket) packet;
                handleLoginResponsePacket(pk);
                break;
            }

            case ICommand.MESSAGE_REQUEST: {
                MessageRequestPacket pk = (MessageRequestPacket) packet;
                handleMessageRequestPacket(pk);
                break;
            }
            case ICommand.MESSAGE_RESPONSE: {
                MessageResponsePacket pk = (MessageResponsePacket) packet;
                handleMessageResponsePacket(pk);
                break;
            }

            case ICommand.BROADCAST_REQUEST: {
                BroadcastRequestPacket pk = (BroadcastRequestPacket) packet;
                new Thread(() -> {
                    handleBroadcastRequestPacket(pk);
                }).start();
                break;
            }
            case ICommand.BROADCAST_RESPONSE: {
                BroadcastResponsePacket pk = (BroadcastResponsePacket) packet;
                handleBroadcastResponsePacket(pk);
                break;
            }

            default:
                break;
        }

        if (IStatus.isReturnStatus(packet))
            callback(packet);

    }

    private void handleBroadcastResponsePacket(BroadcastResponsePacket pk) {
        LogTools.NLogInfo("收到广播消息: " + pk.getMessage());
    }

    private void handleBroadcastRequestPacket(BroadcastRequestPacket pk) {
        var client = Server.Instance.clients.get(pk.getOwnerGuid());
        var msg = "<玩家-" + pk.getOwnerGuid() + "-" + client.name + ">广播: " + pk.getMessage();
        LogTools.NLogInfo("收到" + msg);
        //遍历所有客户端并广播消息
        Server.Instance.clients.forEach((guid, v) -> {
            var bpk = new BroadcastResponsePacket(guid, IStatus.RETURN_SUCCESS, msg);
            Server.Instance.sendPacket(bpk);
        });
    }

    private void handleMessageResponsePacket(MessageResponsePacket pk) {
    }

    private void handleMessageRequestPacket(MessageRequestPacket pk) {
        var client = Server.Instance.clients.get(pk.getOwnerGuid());
        LogTools.NLogInfo("收到<玩家-" + pk.getOwnerGuid() + "-" + client.name + ">信息: " + pk.getMessage());
        var rep = new MessageResponsePacket(pk.getOwnerGuid(), IStatus.RETURN_SUCCESS);
        Server.Instance.sendPacket(rep);
    }

    private void handleLoginResponsePacket(LoginResponsePacket pk) {
        Client.Instance.setOwnerGuid(pk.getOwnerGuid());
        if (IStatus.isSuccessStatus(pk)) LogTools.NLogInfo("登陆响应: 已分配guid: " + pk.getOwnerGuid());
    }

    private void handleLoginRequestPacket(LoginRequestPacket pk, InetSocketAddress sender) {
        var status = loginAuthentication(pk, sender);
        var newGuid = pk.getOwnerGuid();
        if (IStatus.isSuccessStatus(status)) {
            //挤掉之前的同ip号
            var repeatClient = Server.Instance.clients.entrySet().stream().filter(p -> p.getValue().address.equals(sender)).findFirst();
            if (repeatClient.isPresent()) {
                LogTools.NLogDebug("同ip之前帐号自动被挤号.");
                status = IStatus.RETURN_SUCCESS_LOGIN_PUSHTOP;
                Server.Instance.clients.remove(pk.getOwnerGuid());
            }
            newGuid = Server.Instance.endGuid++;
            Server.Instance.loginClient(pk, sender, newGuid);
        }

        var rep = new LoginResponsePacket(newGuid, status);
        Server.Instance.sendPacket(rep);
    }

    private int loginAuthentication(LoginRequestPacket pk, InetSocketAddress sender) {
        var statusId = IStatus.RETURN_SUCCESS;
        var isOnline = Server.Instance.isOnline(pk.getOwnerGuid());
//        var ipRepeat = Server.Instance.clients.values().stream().anyMatch(p->p.address.equals(sender));
        if (isOnline) statusId = IStatus.RETURN_DEFEAT_LOGIN_REPEAT_SAMEGUID;
//        else if(ipRepeat) statusId = IStatus.RETURN_DEFEAT_LOGIN_REPEAT_SAMEADDRESS;
        LogTools.NLogInfo(IStatus.getStatusMsg(pk, statusId));
        return statusId;
    }

    //拦截非登录用户包(除正在登录)
    private boolean strangerInterceptor(Packet packet) {
        if (packet instanceof LoginRequestPacket) return false;
        var playerGuid = packet.getOwnerGuid();
        return !Server.Instance.isOnline(playerGuid);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LogTools.NLogErr("处理器异常: ");
        cause.printStackTrace();
    }

    @SuppressWarnings("unchecked")
    public <T extends Packet> void addCallbackListener(Class<T> ppid, PacketCallback2<T> callback) {
        if (!callbacks.containsKey(ppid)) callbacks.put(ppid, new ArrayList<>());
        var pkCallbacks = callbacks.get(ppid);
        pkCallbacks.add((pk) -> {
            LogTools.NLogDebug(IStatus.getStatusMsg(pk));  //响应信息
            callback.callback((T) pk);
        });
    }

    @SuppressWarnings("unchecked")
    private <T extends Packet> void callback(T pk) {
        var pkCallbacks = callbacks.get(pk.getClass());
        if (pkCallbacks != null && !pkCallbacks.isEmpty()) {
            pkCallbacks.forEach((p) -> p.callback(pk));
            pkCallbacks.clear();
        }
    }
}
