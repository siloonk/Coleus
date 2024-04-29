package io.siloonk.github;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            MinecraftServer server = new MinecraftServer(25565);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
