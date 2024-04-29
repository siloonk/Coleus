package io.siloonk.github.protocol.packets.clientbound.status;

import com.google.gson.Gson;
import io.siloonk.github.protocol.data.StatusResponse;
import io.siloonk.github.protocol.packets.Packet;
import io.siloonk.github.utils.AdventureUtils;
import net.kyori.adventure.text.Component;

import java.io.IOException;

public class StatusResponsePacket extends Packet {


    public StatusResponsePacket(StatusResponse response) throws IOException {
        super(0x00);
        getOut().writeString(response.toJson());
    }

}
