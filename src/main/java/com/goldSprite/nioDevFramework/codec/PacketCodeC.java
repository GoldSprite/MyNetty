package com.goldSprite.nioDevFramework.codec;

import com.goldSprite.nioDevFramework.codec.codecInterfaces.ISerializer;
import com.goldSprite.nioDevFramework.codec.codecInterfaces.Packet;
import com.goldSprite.nioDevFramework.codec.packets.*;
import com.goldSprite.nioDevFramework.codec.serializers.JSONSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import static com.goldSprite.nioDevFramework.codec.codecInterfaces.ICommand.*;

public class PacketCodeC {
    public static final int MAGIC_NUMBER = 0x31699784;  //0x开头接8位数: 编码识别id
    public static final PacketCodeC INSTANCE = new PacketCodeC();
    private final Map<Byte, ISerializer> serializerMap = new HashMap<>();  //编码器id
    private final Map<Byte, Class<? extends Packet>> packetTypeMap = new HashMap<>();  //包类型id

    public PacketCodeC() {
        ISerializer serializer = new JSONSerializer();
        serializerMap.put(serializer.getSerializerAlgorithm(), serializer);

        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);
        packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(BROADCAST_REQUEST, BroadcastRequestPacket.class);
        packetTypeMap.put(BROADCAST_RESPONSE, BroadcastResponsePacket.class);
    }

    public ByteBuf encode(ByteBufAllocator bufAllocator, Packet packet) {
        try {
            // 1. 创建 ByteBuf 对象
            ByteBuf byteBuf = bufAllocator.ioBuffer();
            // 2. 序列化 Java 对象
            byte[] bytes = ISerializer.DEFAULT.serialize(packet);
            // 3. 实际编码过程，把通信协议几个部分，一一编码
            byteBuf.writeInt(MAGIC_NUMBER);
            byteBuf.writeByte(packet.getVersion());
            byteBuf.writeByte(ISerializer.DEFAULT.getSerializerAlgorithm());
            byteBuf.writeByte(packet.getCommand());
            byteBuf.writeInt(bytes.length);
            byteBuf.writeBytes(bytes);
            return byteBuf;
        } catch (Exception e) {
            return null;
        }
    }

    //解码
    public Packet decode(ByteBuf byteBuf) {
        try {
            // 跳过魔数
            byteBuf.skipBytes(4);
            // 跳过版本号
            byteBuf.skipBytes(1);
            // 序列化算法标识
            byte serializeAlgorithm = byteBuf.readByte();
            // 指令
            byte command = byteBuf.readByte();
            // 数据包长度
            int length = byteBuf.readInt();
            byte[] bytes = new byte[length];
            byteBuf.readBytes(bytes);
            Class<? extends Packet> requestType = getRequestType(command);
            ISerializer serializer = getSerializer(serializeAlgorithm);
            if (requestType != null && serializer != null) {
                return serializer.deserialize(bytes, requestType);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    private ISerializer getSerializer(byte serializerAlgorithm) {
        if (!serializerMap.containsKey(serializerAlgorithm)) return null;
        return serializerMap.get(serializerAlgorithm);
    }

    private Class<? extends Packet> getRequestType(byte command) {
        if (!packetTypeMap.containsKey(command)) return null;
        return packetTypeMap.get(command);
    }

    public DatagramPacket encodeDpk(ByteBufAllocator alloc, Packet pk, InetSocketAddress target) {
        var buf = encode(alloc, pk);
        var dpk = new DatagramPacket(buf, target);
        return dpk;
    }
}
