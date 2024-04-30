package io.siloonk.github.protocol;

import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PacketWriter {


    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;
    private DataOutputStream out;

    public PacketWriter(OutputStream out) {
        this.out = new DataOutputStream(out);
    }

    public void writeInt(int value) throws IOException {
        this.out.writeInt(value);
    }

    public void writeLong(long value) throws IOException {
        this.out.writeLong(value);
    }

    public void writeByte(int value) throws IOException {
        this.out.write(value);
    }

    public void write(byte[] value) throws IOException {
        this.out.write(value);
    }

    public void writeVarInt(int value) throws IOException {
        while (true) {
            if ((value & ~SEGMENT_BITS) == 0) {
                writeByte(value);
                return;
            }

            writeByte((value & SEGMENT_BITS) | CONTINUE_BIT);

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
        }
    }

    public void writeVarLong(long value) throws IOException {
        while (true) {
            if ((value & ~((long) SEGMENT_BITS)) == 0) {
                writeByte((int) value);
                return;
            }

            writeByte((int) ((value & SEGMENT_BITS) | CONTINUE_BIT));

            // Note: >>> means that the sign bit is shifted with the rest of the number rather than being left alone
            value >>>= 7;
        }
    }

    public void writeString(String message) throws IOException {
        writeVarInt(message.length());
        write(message.getBytes(StandardCharsets.UTF_8));
    }

    public void writePosition(int x, int y, int z) throws IOException {
        out.writeLong(((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (y & 0xFFF));
    }

    public void writeUUID(UUID uuid) throws IOException {
        out.writeLong(uuid.getMostSignificantBits());
        out.writeLong(uuid.getLeastSignificantBits());
    }

    public void writeNBTCompound(CompoundBinaryTag compound) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BinaryTagIO.writer().writeNameless(compound, stream);
        out.write(stream.toByteArray());
    }

    public void writeUnsignedByte(int value) throws IOException {
        if (value > -1 && value < 256) {
            out.write(value);
        }
    }
}
