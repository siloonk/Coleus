package io.siloonk.github.protocol.packets.serverbound.configuration;

import io.siloonk.github.protocol.packets.Packet;
import lombok.Getter;

import java.io.IOException;

@Getter
public class ConfigurationClientInformationPacket extends Packet {

    private String locale;
    private int viewDistance;
    private int chatMode;
    private boolean chatColors;
    private int displayedSkinParts;
    private int mainHand;
    private boolean textFiltering;
    private boolean allowServerListing;


    public ConfigurationClientInformationPacket(byte[] data) throws IOException {
        super(data);

        this.locale = getIn().readString();
        this.viewDistance = getIn().readByte();
        this.chatMode = getIn().readVarInt();
        this.chatColors = getIn().readBool();
        this.displayedSkinParts = getIn().readUnsignedByte();
        this.mainHand = getIn().readVarInt();
        this.textFiltering = getIn().readBool();
        this.allowServerListing = getIn().readBool();
    }
}
