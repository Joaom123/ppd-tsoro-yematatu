package com.ifce.ppd.tsoroyematatu.server;

import com.ifce.ppd.tsoroyematatu.constants.MESSAGE_TYPES;
import com.ifce.ppd.tsoroyematatu.models.Client;
import com.ifce.ppd.tsoroyematatu.models.Room;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Handles the connection for each connected client, so the server
 * can handle multiple clients at the same time
 */
public class PlayerThread extends Thread {
    private final Server server;
    private final Socket socket;
    private ObjectOutputStream outputStream;
    private Client client;

    public PlayerThread(Server server, Socket socket) {
        this.socket = socket;
        this.server = server;

        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte socketTypeFlag = 0;
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                socketTypeFlag = inputStream.readByte();
                if (socketTypeFlag == MESSAGE_TYPES.INIT.getFlag()) {
                    client = (Client) inputStream.readObject();
                    String roomId = inputStream.readUTF();
                    Room room = server.createRoom(roomId);
                    System.out.println(String.format("Cliente %s inicializado no servidor", client.getName()));
                    // TODO Give id to client
                }

                if (socketTypeFlag == MESSAGE_TYPES.MESSAGE.getFlag()) {
                    String message = inputStream.readUTF();
                    System.out.println(client.getName() + "> " + message);
                    server.sendMessageToRivalPlayer(message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeByte(MESSAGE_TYPES.MESSAGE.getFlag());
            outputStream.writeUTF(message);
            outputStream.writeObject(client);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
