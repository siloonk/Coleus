package io.siloonk.github.protocol;

import io.siloonk.github.ClientSession;
import io.siloonk.github.Player;
import io.siloonk.github.PlayerSettings;
import io.siloonk.github.protocol.annotations.PacketReceiver;
import io.siloonk.github.protocol.data.StatusResponse;
import io.siloonk.github.protocol.packets.Packet;
import io.siloonk.github.protocol.packets.clientbound.configuration.ClientBoundConfigurationPluginMessagePacket;
import io.siloonk.github.protocol.packets.clientbound.configuration.ConfigurationDisconnectPacket;
import io.siloonk.github.protocol.packets.clientbound.configuration.FinishConfigurationPacket;
import io.siloonk.github.protocol.packets.clientbound.configuration.RegistryDataPacket;
import io.siloonk.github.protocol.packets.clientbound.login.LoginDisconnectPacket;
import io.siloonk.github.protocol.packets.clientbound.login.LoginSuccessPacket;
import io.siloonk.github.protocol.packets.clientbound.status.StatusPingResponsePacket;
import io.siloonk.github.protocol.packets.clientbound.status.StatusResponsePacket;
import io.siloonk.github.protocol.packets.serverbound.HandshakePacket;
import io.siloonk.github.protocol.packets.serverbound.configuration.AcknowledgeFinishConfigurationPacket;
import io.siloonk.github.protocol.packets.serverbound.configuration.ConfigurationClientInformationPacket;
import io.siloonk.github.protocol.packets.serverbound.configuration.ConfigurationPluginMessagePacket;
import io.siloonk.github.protocol.packets.serverbound.configuration.ConfigurationServerBoundKeepAlivePacket;
import io.siloonk.github.protocol.packets.serverbound.login.LoginAcknowledgedPacket;
import io.siloonk.github.protocol.packets.serverbound.login.LoginStartPacket;
import io.siloonk.github.protocol.packets.serverbound.status.StatusPingRequestPacket;
import io.siloonk.github.protocol.packets.serverbound.status.StatusRequestPacket;
import io.siloonk.github.utils.AdventureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public class PacketHandler {

    private static final Logger logger = LoggerFactory.getLogger(PacketHandler.class);

    private ClientSession client;
    private Player player;

    public PacketHandler(ClientSession client) {
        this.client = client;
    }

    @PacketReceiver
    public void onHandshake(HandshakePacket packet) throws IOException {
        logger.info("Received handshaking packet with version %d, next state: %d".formatted(packet.getVersion(), packet.getNextState()));
        if (packet.getNextState() == 1) client.setState(GameState.STATUS);
        else client.setState(GameState.LOGIN);
        client.setProtocolVersion(packet.getVersion());
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

    @PacketReceiver
    public void onLoginStart(LoginStartPacket packet) throws IOException {
        logger.info("Received login start packet with username %s and UUID %s".formatted(packet.getUsername(), packet.getUuid()));
        if (client.getProtocolVersion() != client.getServer().getProtocolVersion()) {
            client.sendPacket(new LoginDisconnectPacket(AdventureUtils.serializeString("<red>You are using an incorrect version, please come back with 1.20.4!")));
            return;
        }

        if (client.getServer().isOnline(packet.getUuid())) {
            client.sendPacket(new LoginDisconnectPacket(AdventureUtils.serializeString("<red>You are already using this name on the server!")));
            return;
        }

        Player player = new Player(packet.getUsername(), packet.getUuid(), client);
        client.getServer().addPlayer(player);
        client.sendPacket(new LoginSuccessPacket(player));
        this.player = player;
    }


    @PacketReceiver
    public void onLoginAcknowledged(LoginAcknowledgedPacket packet) throws IOException {
        client.setState(GameState.CONFIGURATION);
        client.startConfigurationKeepAlive();
        client.sendPacket(new RegistryDataPacket(client.getServer().REGISTRY_NBT));
        client.sendPacket(new ClientBoundConfigurationPluginMessagePacket("minecraft:brand", "Coleus Server".getBytes(StandardCharsets.UTF_8)));
        client.sendPacket(new FinishConfigurationPacket());
    }

    @PacketReceiver
    public void onConfigurationClientInformation(ConfigurationClientInformationPacket packet) {
        PlayerSettings settings = new PlayerSettings(packet.getLocale(), packet.getViewDistance(), packet.getChatMode(), packet.isChatColors(), packet.getDisplayedSkinParts(), packet.getMainHand(), packet.isTextFiltering(), packet.isAllowServerListing());
        player.setSettings(settings);
    }

    @PacketReceiver
    public void onConfigurationKeepAlivePacket(ConfigurationServerBoundKeepAlivePacket packet) throws IOException {
        if (packet.getPayload() != client.getLastKeepAlive()) {
            // Disconnect player
            player.getHandler().sendPacket(new ConfigurationDisconnectPacket(AdventureUtils.serializeString("<red>Failed keep alive\nExpected Payload: %d received: %d".formatted(client.getLastKeepAlive(), packet.getPayload()))));
            return;
        }
    }

    @PacketReceiver
    public void onConfigurationFinished(AcknowledgeFinishConfigurationPacket packet) {
        logger.info("Received Acknowledge Finish Configuration Packet!");
        client.setState(GameState.PLAY);
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
