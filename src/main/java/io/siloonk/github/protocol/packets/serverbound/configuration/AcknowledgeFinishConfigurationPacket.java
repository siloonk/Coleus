package io.siloonk.github.protocol.packets.serverbound.configuration;

import io.siloonk.github.protocol.packets.Packet;

public class AcknowledgeFinishConfigurationPacket extends Packet {
    public AcknowledgeFinishConfigurationPacket(byte[] data) {
        super(data);
    }
}
