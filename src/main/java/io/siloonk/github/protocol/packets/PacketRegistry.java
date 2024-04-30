package io.siloonk.github.protocol.packets;

import io.siloonk.github.protocol.GameState;
import io.siloonk.github.protocol.packets.serverbound.HandshakePacket;
import io.siloonk.github.protocol.packets.serverbound.configuration.AcknowledgeFinishConfigurationPacket;
import io.siloonk.github.protocol.packets.serverbound.configuration.ConfigurationClientInformationPacket;
import io.siloonk.github.protocol.packets.serverbound.configuration.ConfigurationPluginMessagePacket;
import io.siloonk.github.protocol.packets.serverbound.configuration.ConfigurationServerBoundKeepAlivePacket;
import io.siloonk.github.protocol.packets.serverbound.login.LoginAcknowledgedPacket;
import io.siloonk.github.protocol.packets.serverbound.login.LoginStartPacket;
import io.siloonk.github.protocol.packets.serverbound.status.StatusPingRequestPacket;
import io.siloonk.github.protocol.packets.serverbound.status.StatusRequestPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class PacketRegistry {

    private static final Map<GameState, Map<Integer, Class<? extends Packet>>> PACKETS = new HashMap<>();

    static {
        PACKETS.put(GameState.HANDSHAKE, new HashMap<>(){
            {
                put(0x00, HandshakePacket.class);
            }
        });

        PACKETS.put(GameState.STATUS, new HashMap<>(){
            {
                put(0x00, StatusRequestPacket.class);
                put(0x01, StatusPingRequestPacket.class);
            }
        });

        PACKETS.put(GameState.LOGIN, new HashMap<>(){
            {
                put(0x00, LoginStartPacket.class);
                put(0x03, LoginAcknowledgedPacket.class);
            }
        });

        PACKETS.put(GameState.CONFIGURATION, new HashMap<>(){
            {
                put(0x00, ConfigurationClientInformationPacket.class);
                put(0x01, ConfigurationPluginMessagePacket.class);
                put(0x02, AcknowledgeFinishConfigurationPacket.class);
                put(0x03, ConfigurationServerBoundKeepAlivePacket.class);
            }
        });

        PACKETS.put(GameState.PLAY, new HashMap<>(){
            {

            }
        });
    }

    public static Class<? extends Packet> getPacketForId(GameState state, int id) {
        return PACKETS.getOrDefault(state, new WeakHashMap<>()).get(id);
    }
}
