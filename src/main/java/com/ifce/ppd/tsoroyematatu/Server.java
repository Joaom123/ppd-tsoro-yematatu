package com.ifce.ppd.tsoroyematatu;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Starts the server, listening to port 12345.
 * When a new client is connected, an instace of PlayerThread is created.
 * Each connection is processed in a separeted thread.
 */
public class Server implements RMIInterface {
    private final Set<Room> rooms = Collections.synchronizedSet(new HashSet<>());

    public static void main(String[] args) {
        /*Server server = new Server();
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            Server obj = new Server();
            RMIInterface stub = (RMIInterface) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("RMIInterface", stub);

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(12345);
        System.out.println("Servidor inicializado na porta 12345!");

        //noinspection InfiniteLoopStatement
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Cliente conectado: " + socket.getInetAddress().getHostAddress());

            PlayerThread playerThread = new PlayerThread(this, socket); // Start player's thread
            playerThread.start();
        }

    }

    /**
     * If a room with given roomId doesn't exist, create a new one. If it does, return it.
     *
     * @param roomId The room's id
     * @return The created room or the one with given roomId
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

    @Override
    public String sayHello() throws RemoteException {
        return "Hello, world!";
    }
}

