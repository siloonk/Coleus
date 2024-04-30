package io.siloonk.github;


import io.siloonk.github.utils.ResourceUtils;
import lombok.Getter;
import net.kyori.adventure.nbt.BinaryTagIO;
import net.kyori.adventure.nbt.CompoundBinaryTag;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MinecraftServer {

    private final ServerSocket server;
    private static final Logger logger = LoggerFactory.getLogger(MinecraftServer.class);
    private Executor executorPool = Executors.newCachedThreadPool();

    @Getter
    private final int protocolVersion = 765;

    public CompoundBinaryTag REGISTRY_NBT;


    HashMap<UUID, Player> players = new HashMap<>();


    public MinecraftServer(int port) throws IOException {
        logger.info("Binding socket to 127.0.0.1:%d".formatted(port));
        server = new ServerSocket();
        server.bind(new InetSocketAddress(port));
    }

    public void start() throws IOException {
        loadRegistry();

        // Listen for connections
        while (!isClosed())  {
            Socket client = server.accept();
            ClientSession session = new ClientSession(client, this);
            logger.info("Client connection made!");
            executorPool.execute(session::handle);
        }
    }

    private void loadRegistry() {
        try (InputStream inputStream = ResourceUtils.getResourceAsStream("/networkCodec.nbt")) {
            if (inputStream == null) return;

            InputStream stream = ResourceUtils.getResourceAsStream("/networkCodec.nbt");

            REGISTRY_NBT = BinaryTagIO.reader().read(stream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOnline(UUID player) {
        return players.containsKey(player);
    }

    @Nullable
    public Player getPlayer(UUID player) {
        return players.get(player);
    }


    public void addPlayer(Player player) {
        this.players.put(player.getUuid(), player);
    }


    public boolean isClosed() {
        return !server.isBound() && server.isClosed();
    }
}
