package com.ifce.ppd.tsoroyematatu.server;

import com.ifce.ppd.tsoroyematatu.models.Room;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Starts the server, listening on a specific port.
 * When a new client is connected, an instace of PlayerThread is created.
 * Since each connection is processed in a separeted thread, the server is able to handle multiple clients.
 */
public class Server {
    private final Set<Room> rooms = Collections.synchronizedSet(new HashSet<>());
    private final Set<PlayerThread> playerThreads = new HashSet<>();
    private ServerSocket serverSocket;

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
     * If a room with given roomId doesn't exist, create a new one. If it does, return it.
     *
     * @param roomId The room's id
     * @return The room
     */
    public Room createRoom(String roomId) {
        // If room already exist, return it
        for (Room room : rooms)
            if (room.getId().equals(roomId))
                return room;

        // Create a new room if it doesn't exist
        Room room = new Room(roomId);
        rooms.add(room);
        System.out.println("Sala " + room.getId() + " foi criada.");
        return room;
    }
}

