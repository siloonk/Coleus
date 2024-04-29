package io.siloonk.github.protocol.packets.clientbound.status;

import io.siloonk.github.protocol.packets.Packet;

import java.io.IOException;

public class StatusPingResponsePacket extends Packet {

    public StatusPingResponsePacket(long paylaod) throws IOException {
        super(0x01);
        getOut().writeLong(paylaod);
    }
}
