package io.siloonk.github.protocol.packets.clientbound.play;

import io.siloonk.github.protocol.packets.Packet;

import java.io.IOException;

public class GameEventPacket extends Packet {

    public GameEventPacket(int event, float value) throws IOException {
        super(0x20);
        getOut().writeUnsignedByte(event)
    }

    public GameEventPacket(int event) throws IOException {
        super(0x20);
    }
}
