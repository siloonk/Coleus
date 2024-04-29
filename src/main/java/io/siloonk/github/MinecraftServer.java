package io.siloonk.github;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MinecraftServer {

    private final ServerSocket server;
    private static final Logger logger = LoggerFactory.getLogger(MinecraftServer.class);
    private Executor executorPool = Executors.newCachedThreadPool();



    public MinecraftServer(int port) throws IOException {
        logger.info("Binding socket to 127.0.0.1:%d".formatted(port));
        server = new ServerSocket();
        server.bind(new InetSocketAddress(port));
    }

    public void start() throws IOException {

        // Listen for connections
        while (!isClosed())  {
            Socket client = server.accept();
            ClientSession session = new ClientSession(client);
            logger.info("Client connection made!");
            executorPool.execute(session::handle);
        }
    }

    public boolean isClosed() {
        return !server.isBound() && server.isClosed();
    }
}
