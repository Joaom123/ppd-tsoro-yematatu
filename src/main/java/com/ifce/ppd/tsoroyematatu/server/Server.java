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
    private final Set<String> userNames = new HashSet<>();
    private final Set<PlayerThread> playerThreads = new HashSet<>();

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
//        try {
//            while (true) {
//                ObjectInputStream inputStream = new ObjectInputStream(client.getInputStream());
//
//                byte socketTypeFlag = inputStream.readByte();
//
//                if (socketTypeFlag == SOCKET_TYPES.INIT.getFlag()) {
//                    System.out.println("Cliente inicializado no servidor");
//                    System.out.println(inputStream.readObject());
//                }
//                if (socketTypeFlag == SOCKET_TYPES.MESSAGE.getFlag()) {
//                    System.out.println("Mensagem recebida");
//                    String message = inputStream.readUTF();
//                    System.out.println(message);
//                    //Send message to other player
//                }
//
//
//            }
//        } catch (Exception e) {
//            System.out.println("Error " + e.getMessage());
//        }
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
}

