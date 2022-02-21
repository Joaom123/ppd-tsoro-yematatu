package com.ifce.ppd.tsoroyematatu.server;

import com.ifce.ppd.tsoroyematatu.constants.MESSAGE_TYPES;
import com.ifce.ppd.tsoroyematatu.exceptions.NoRivalException;
import com.ifce.ppd.tsoroyematatu.models.Client;
import com.ifce.ppd.tsoroyematatu.models.Room;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Handles the connection for each connected player(client).
 */
public class PlayerThread extends Thread {
    private final Server server;
    private final Socket socket;
    private ObjectOutputStream outputStream;
    private Client client;
    private Room room;

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
                    client = (Client) inputStream.readObject(); // set client
                    System.out.println("Cliente " + client.getName() + " inicializado no servidor");

                    String roomId = inputStream.readUTF();
                    room = server.createRoom(roomId); // set room

                    room.addPlayer(this); // add player to the room
                    System.out.println("Cliente " + client.getName() + " entrou na sala " + room.getId());
                }

                if (socketTypeFlag == MESSAGE_TYPES.MESSAGE.getFlag()) {
                    String message = inputStream.readUTF();
                    sendMessageToRivalPlayer(client.getName(), message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send a message to this plauerThread's client.
     *
     * @param author  The author of the message, can be the server or another player
     * @param message The message to be passed
     */
    public void sendMessage(String author, String message) {
        try {
            outputStream.writeByte(MESSAGE_TYPES.MESSAGE.getFlag());
            outputStream.writeUTF(author);
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send a message to the rival player in the room
     *
     * @param message The message to be sent.
     */
    public void sendMessageToRivalPlayer(String author, String message) {
        try {
            room.getRivalPlayerThread(this).sendMessage(author, message);
        } catch (NoRivalException e) {
            e.printStackTrace();
        }
    }
}
