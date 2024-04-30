package io.siloonk.github.protocol.packets.serverbound.configuration;

import io.siloonk.github.protocol.packets.Packet;
import lombok.Getter;

import java.io.IOException;

@Getter
public class ConfigurationPluginMessagePacket extends Packet {

    String identifier;
    byte[] data;

    public ConfigurationPluginMessagePacket(byte[] data) throws IOException {
        super(data);

        identifier = getIn().readString();
        if (data.length != identifier.length()) {
            data = getIn().readAllBytes();
        }
    }
}
