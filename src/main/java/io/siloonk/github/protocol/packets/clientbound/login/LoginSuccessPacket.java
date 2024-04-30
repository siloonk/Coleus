package io.siloonk.github.protocol.packets.clientbound.login;

import io.siloonk.github.Player;
import io.siloonk.github.protocol.packets.Packet;

import java.io.IOException;

public class LoginSuccessPacket extends Packet {


    public LoginSuccessPacket(Player player) throws IOException {
        super(0x02);
        getOut().writeUUID(player.getUuid());
        getOut().writeString(player.getUsername());
        getOut().writeVarInt(0);
    }
}
