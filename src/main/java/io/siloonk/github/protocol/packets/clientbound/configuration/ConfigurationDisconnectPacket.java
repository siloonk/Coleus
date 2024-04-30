package io.siloonk.github.protocol.packets.clientbound.configuration;

import io.siloonk.github.protocol.packets.Packet;
import io.siloonk.github.utils.AdventureUtils;
import net.kyori.adventure.Adventure;
import net.kyori.adventure.text.Component;

import java.io.IOException;

public class ConfigurationDisconnectPacket extends Packet {


    public ConfigurationDisconnectPacket(Component reason) throws IOException {
        super(0x01);
        getOut().writeString(AdventureUtils.serializeJson(reason));
    }
}
