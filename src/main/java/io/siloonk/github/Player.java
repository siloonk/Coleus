package io.siloonk.github;

import io.siloonk.github.protocol.Location;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Player {

    private String username;
    @Setter
    private String displayName;
    private UUID uuid;

    @Setter
    private PlayerSettings settings;

    private ClientSession handler;

    @Setter
    private Location position;

    public Player(String username, UUID uuid, ClientSession handler) {
        this.username = username;
        this.uuid = uuid;
        this.displayName = username;
        this.handler = handler;
    }
}
