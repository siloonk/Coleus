package io.siloonk.github.protocol.packets.serverbound.status;

import io.siloonk.github.protocol.packets.Packet;

public class StatusRequestPacket extends Packet {
    public StatusRequestPacket(byte[] data) {
        super(data);
    }
}
