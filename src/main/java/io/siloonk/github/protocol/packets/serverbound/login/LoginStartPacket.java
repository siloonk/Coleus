package io.siloonk.github.protocol.packets.serverbound.login;

import io.siloonk.github.protocol.packets.Packet;
import lombok.Getter;

import java.io.IOException;
import java.util.UUID;

@Getter
public class LoginStartPacket extends Packet {

    private String username;
    private UUID uuid;

    public LoginStartPacket(byte[] data) throws IOException {
        super(data);

        this.username = getIn().readString();
        this.uuid = getIn().readUUID();
    }
}
