package io.siloonk.github;

import io.siloonk.github.protocol.GameState;
import io.siloonk.github.protocol.PacketHandler;
import io.siloonk.github.protocol.PacketReader;
import io.siloonk.github.protocol.PacketWriter;
import io.siloonk.github.protocol.packets.Packet;
import io.siloonk.github.protocol.packets.PacketRegistry;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class ClientSession {

    private static final Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ClientSession.class);

    private Socket socket;
    @Setter
    @Getter
    private GameState state;

    private PacketReader in;
    private PacketWriter out;
    private PacketHandler packetHandler;

    public ClientSession(Socket socket) throws IOException {
        this.socket = socket;
        this.state = GameState.HANDSHAKE;

        this.in = new PacketReader(socket.getInputStream());
        this.out = new PacketWriter(socket.getOutputStream());
        this.packetHandler = new PacketHandler(this);
    }

    public void handle() {
        try {
            while (!isClosed()) {
                if (in.bytesAvailable() == 0) continue;
                int packetLength = in.readVarInt();
                int packetID = in.readVarInt();
                byte[] data = new byte[packetLength - in.getVarIntSize(packetID)];
                in.readAll(data);

                Class<? extends Packet> packetClass = PacketRegistry.getPacketForId(state, packetID);
                if (packetClass == null) {
                    logger.info("Received unknown packet with id %d and length %d with data %s".formatted(packetID, packetLength, Arrays.toString(data)));
                    continue;
                }
                Packet packet = packetClass.getConstructor(byte[].class).newInstance(data);
                packetHandler.handle(packet);


            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPacket(Packet packet) throws IOException {
        out.write(packet.getData());
    }


    public boolean isClosed() {
        return socket.isClosed() && !socket.isConnected();
    }
}
