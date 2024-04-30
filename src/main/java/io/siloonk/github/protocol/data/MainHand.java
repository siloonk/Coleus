package io.siloonk.github.protocol.data;

public enum MainHand {
    LEFT, RIGHT;

    public static MainHand of(int value) {
        return switch (value) {
            case 0 -> LEFT;
            case 1 -> RIGHT;
            default -> null;
        };
    }
}
