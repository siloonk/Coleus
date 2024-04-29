package io.siloonk.github.protocol.data;

import com.google.gson.Gson;
import io.siloonk.github.protocol.packets.clientbound.status.StatusResponsePacket;
import io.siloonk.github.utils.AdventureUtils;
import net.kyori.adventure.text.Component;

public class StatusResponse {

    private final Players players;
    private final Version version;
    private final String description;

    public StatusResponse(Players players, Version version, Component description) {
        this.players = players;
        this.version = version;
        this.description = AdventureUtils.serializeJson(description);
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public record Players(int online, int max) {}
    public record Version(int protocol, String name) {}
}
