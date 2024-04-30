package io.siloonk.github.protocol.packets.clientbound.configuration;

import io.siloonk.github.protocol.packets.Packet;

import java.io.IOException;

public class ClientBoundConfigurationPluginMessagePacket extends Packet {


    public ClientBoundConfigurationPluginMessagePacket(String identifier, byte[] data) throws IOException {
        super(0x00);

        getOut().writeString(identifier);
        getOut().writeVarInt(data.length);
        getOut().write(data);
    }
}
