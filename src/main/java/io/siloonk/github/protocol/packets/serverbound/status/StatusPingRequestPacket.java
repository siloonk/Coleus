package io.siloonk.github.protocol.packets.serverbound.status;

import io.siloonk.github.protocol.packets.Packet;
import lombok.Getter;

import java.io.IOException;

public class StatusPingRequestPacket extends Packet {

    @Getter
    private long payload;

    public StatusPingRequestPacket(byte[] data) throws IOException {
        super(data);

        payload = getIn().readLong();
    }
}
