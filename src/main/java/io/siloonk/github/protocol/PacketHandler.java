package io.siloonk.github.protocol;

import io.siloonk.github.ClientSession;
import io.siloonk.github.MinecraftServer;
import io.siloonk.github.protocol.annotations.PacketReceiver;
import io.siloonk.github.protocol.data.StatusResponse;
import io.siloonk.github.protocol.packets.Packet;
import io.siloonk.github.protocol.packets.clientbound.status.StatusPingResponsePacket;
import io.siloonk.github.protocol.packets.clientbound.status.StatusResponsePacket;
import io.siloonk.github.protocol.packets.serverbound.HandshakePacket;
import io.siloonk.github.protocol.packets.serverbound.status.StatusPingRequestPacket;
import io.siloonk.github.protocol.packets.serverbound.status.StatusRequestPacket;
import io.siloonk.github.utils.AdventureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PacketHandler {

    private static final Logger logger = LoggerFactory.getLogger(PacketHandler.class);

    private ClientSession client;

    public PacketHandler(ClientSession client) {
        this.client = client;
    }

    @PacketReceiver
    public void onHandshake(HandshakePacket packet) throws IOException {
        logger.info("Received handshaking packet with version %d, next state: %d".formatted(packet.getVersion(), packet.getNextState()));
        if (packet.getNextState() == 1) client.setState(GameState.STATUS);
        else client.setState(GameState.LOGIN);
    }

    @PacketReceiver
    public void onStatusRequest(StatusRequestPacket packet) throws IOException {
        logger.info("Received status request packet!");
        client.sendPacket(new StatusResponsePacket(
                new StatusResponse(
                    new StatusResponse.Players(0, 2024),
                    new StatusResponse.Version(765, "Coleus - 1.20.4"),
                    AdventureUtils.serializeString("Welcome to Coleus!")
        )));
    }

    @PacketReceiver
    public void onStatusPingRequest(StatusPingRequestPacket packet) throws IOException {
        logger.info("Received status ping request with payload %d".formatted(packet.getPayload()));
        client.sendPacket(new StatusPingResponsePacket(packet.getPayload()));
    }

    public void handle(Packet packet) throws InvocationTargetException, IllegalAccessException {
        for (Method method : getClass().getMethods()) {
            if (!method.isAnnotationPresent(PacketReceiver.class))
                continue;

            Class<?>[] params = method.getParameterTypes();

            if (params.length == 1 && params[0] == packet.getClass())
                method.invoke(this, packet);
        }
    }
}
