package io.siloonk.github.protocol.packets.clientbound.configuration;

import io.siloonk.github.protocol.packets.Packet;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;

import java.io.IOException;

public class RegistryDataPacket extends Packet {

    public RegistryDataPacket(CompoundBinaryTag compound) throws IOException {
        super(0x05);
        getOut().writeNBTCompound(compound);
    }
}
