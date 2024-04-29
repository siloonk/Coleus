package io.siloonk.github.protocol;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class PacketReader {

    private DataInputStream in;

    private static final int SEGMENT_BITS = 0x7F;
    private static final int CONTINUE_BIT = 0x80;

    public PacketReader(InputStream in) {
        this.in = new DataInputStream(in);
    }

    public int readInt() throws IOException {
        return in.readInt();
    }

    public int bytesAvailable() throws IOException {
        return in.available();
    }

    public long readLong() throws IOException {
        return in.readLong();
    }

    public byte readByte() throws IOException {
        return in.readByte();
    }

    public void readAll(byte[] buffer) throws IOException {
        in.readFully(buffer);
    }

    public byte[] readAllBytes() throws IOException {
        return in.readAllBytes();
    }

    public short readShort() throws IOException {
        return in.readShort();
    }

    public int readUnsignedShort() throws IOException {
        return in.readUnsignedByte();
    }

    public int readUnsignedByte() throws IOException {
        return in.readUnsignedByte();
    }

    public String readString() throws IOException {
        int length = readVarInt();
        byte[] data = new byte[length];
        in.readFully(data);
        return new String(data, StandardCharsets.UTF_8);
    }

    public int readVarInt() throws IOException {
        int value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = readByte();
            value |= (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 32) throw new RuntimeException("VarInt is too big");
        }

        return value;
    }


    public long readVarLong() throws IOException {
        long value = 0;
        int position = 0;
        byte currentByte;

        while (true) {
            currentByte = readByte();
            value |= (long) (currentByte & SEGMENT_BITS) << position;

            if ((currentByte & CONTINUE_BIT) == 0) break;

            position += 7;

            if (position >= 64) throw new RuntimeException("VarLong is too big");
        }

        return value;
    }

    public int getVarIntSize(int value) {
        int result = 0;
        do {
            result++;
            value >>>= 7;
        } while (value != 0);
            return result;
    }

    public Location readPosition() throws IOException {
        long val = readLong();
        long x = val >> 38;
        long y = val << 52 >> 52;
        long z = val << 26 >> 38;
        return new Location((int) x, (int) y, (int) z);
    }
}
