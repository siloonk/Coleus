package io.siloonk.github.protocol.packets.serverbound.configuration;

import io.siloonk.github.protocol.packets.Packet;
import lombok.Getter;

import java.io.IOException;

public class ConfigurationServerBoundKeepAlivePacket extends Packet {

    @Getter
    long payload;

    public ConfigurationServerBoundKeepAlivePacket(byte[] data) throws IOException {
        super(data);
        this.payload = getIn().readLong();
    }
}
