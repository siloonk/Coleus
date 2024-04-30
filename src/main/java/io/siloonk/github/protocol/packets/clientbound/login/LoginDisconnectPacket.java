package io.siloonk.github.protocol.packets.clientbound.login;

import io.siloonk.github.protocol.packets.Packet;
import io.siloonk.github.utils.AdventureUtils;
import net.kyori.adventure.text.Component;

import java.io.IOException;

public class LoginDisconnectPacket extends Packet {

    public LoginDisconnectPacket(Component message) throws IOException {
        super(0x00);
        getOut().writeString(AdventureUtils.serializeJson(message));
    }
}
