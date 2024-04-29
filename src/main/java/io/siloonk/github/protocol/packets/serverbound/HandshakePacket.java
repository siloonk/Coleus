package io.siloonk.github.protocol.packets.serverbound;

import io.siloonk.github.protocol.PacketReader;
import io.siloonk.github.protocol.packets.Packet;
import lombok.Getter;

import java.io.IOException;

@Getter
public class HandshakePacket extends Packet {

    private int version;
    private String address;
    private short port;
    private int nextState;

    public HandshakePacket(byte[] data) throws IOException {
        super(data);

        PacketReader in = getIn();
        version = in.readVarInt();
        address = in.readString();
        port = (short) in.readShort();
        nextState = in.readVarInt();
    }
}
