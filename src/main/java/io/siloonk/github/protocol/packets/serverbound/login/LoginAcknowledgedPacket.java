package io.siloonk.github.protocol.packets.serverbound.login;

import io.siloonk.github.protocol.packets.Packet;

public class LoginAcknowledgedPacket extends Packet {
    public LoginAcknowledgedPacket(byte[] data) {
        super(data);
    }
}
