package io.siloonk.github;

import io.siloonk.github.protocol.data.ChatMode;
import io.siloonk.github.protocol.data.MainHand;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerSettings {

    private String locale;
    private int viewDistance;
    private ChatMode chatMode;
    private boolean chatColors;
    private int displayedSkinParts;
    private MainHand mainHand;
    private boolean textFiltering;
    private boolean allowServerListing;

    public PlayerSettings(String locale, int viewDistance, int chatMode, boolean chatColors, int displayedSkinParts, int mainHand, boolean textFiltering, boolean allowServerListing) {
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.chatMode = ChatMode.of(chatMode);
        this.chatColors = chatColors;
        this.displayedSkinParts = displayedSkinParts;
        this.mainHand = MainHand.of(mainHand);
        this.textFiltering = textFiltering;
        this.allowServerListing = allowServerListing;
    }
}
