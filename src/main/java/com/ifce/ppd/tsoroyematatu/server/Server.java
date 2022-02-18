package com.ifce.ppd.tsoroyematatu.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Starts the server, listening on a specific port.
 * When a new client is connected, an instace of PlayerThread is created.
 * Since each connection is processed in a separeted thread, the server is able to handle multiple clients.
 */
public class Server {
    ServerSocket serverSocket;
    private Set<String> userNames = new HashSet<>();
    private Set<PlayerThread> playerThreads = new HashSet<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(12345);
            System.out.println("Servidor inicializado!");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress().getHostAddress());
                PlayerThread playerThread = new PlayerThread(this, socket);
                playerThreads.add(playerThread);
                playerThread.start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * TODO: Remove client from server
     */
    public void removeClient() {
    }

    /**
     * TODO: Save client
     */
    public void saveClient() {

    }

    /**
     * Broadcast menssage
     * @param message
     */
    public void broadcastMessageToRoom(String message) {
        playerThreads.forEach(playerThread -> {
            playerThread.sendMessage(message);
        });
    }
}

