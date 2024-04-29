package io.siloonk.github.protocol.packets;

import io.siloonk.github.protocol.PacketReader;
import io.siloonk.github.protocol.PacketWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Packet {
    PacketReader in;
    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    PacketWriter out = new PacketWriter(buffer);

    public Packet(byte[] data) {
        in = new PacketReader(new ByteArrayInputStream(data));
    }

    public Packet(int id) throws IOException {
        out.writeVarInt(id);
    }

    protected PacketReader getIn() {
        return in;
    }

    protected PacketWriter getOut() {
        return out;
    }

    public byte[] getData() throws IOException {
        byte[] raw = buffer.toByteArray();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PacketWriter writer = new PacketWriter(buffer);
        writer.writeVarInt(raw.length);
        writer.write(raw);
        return buffer.toByteArray();
    }
}
