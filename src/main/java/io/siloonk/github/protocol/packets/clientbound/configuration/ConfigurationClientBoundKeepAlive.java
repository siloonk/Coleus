package io.siloonk.github.protocol.packets.clientbound.configuration;

import io.siloonk.github.protocol.packets.Packet;

import java.io.IOException;

public class ConfigurationClientBoundKeepAlive extends Packet {


    public ConfigurationClientBoundKeepAlive(long payload) throws IOException {
        super(0x03);
        getOut().writeLong(payload);
    }
}
