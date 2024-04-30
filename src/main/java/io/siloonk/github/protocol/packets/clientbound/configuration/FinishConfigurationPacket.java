package io.siloonk.github.protocol.packets.clientbound.configuration;

import io.siloonk.github.protocol.packets.Packet;

import java.io.IOException;

public class FinishConfigurationPacket extends Packet {

    public FinishConfigurationPacket() throws IOException {
        super(0x02);
    }
}
