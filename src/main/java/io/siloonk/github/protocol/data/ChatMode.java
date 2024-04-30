package io.siloonk.github.protocol.data;

public enum ChatMode {
    ENABLED,
    COMMANDS_ONLY,
    HIDDEN;

    public static ChatMode of(int value) {
        return switch(value) {
            case 0 -> ENABLED;
            case 1 -> COMMANDS_ONLY;
            default -> HIDDEN;
        };
    }
}
