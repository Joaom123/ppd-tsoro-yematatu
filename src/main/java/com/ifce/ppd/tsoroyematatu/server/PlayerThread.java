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
        byte inputFlag = 0;
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            try {
                inputFlag = inputStream.readByte();
                if (inputFlag == MESSAGE_TYPES.INIT.getFlag()) {
                    client = (Client) inputStream.readObject(); // set client
                    System.out.println("Cliente " + client.getName() + " inicializado no servidor");

                    String roomId = inputStream.readUTF();
                    room = server.createRoom(roomId); // set room

                    room.addPlayer(this); // add player to the room
                    System.out.println("Cliente " + client.getName() + " entrou na sala " + room.getId());

                    // If the room isn't full, player should wait until another player enter.
                    if (!room.isFull())
                        sendWaitingFlag();
                    else
                        room.sendPlayable();
                }

                if (inputFlag == MESSAGE_TYPES.MESSAGE.getFlag()) {
                    String message = inputStream.readUTF();
                    sendMessageToRivalPlayer(client.getName(), message);
                }

                if (inputFlag == MESSAGE_TYPES.MOVE.getFlag()) {
                    String pieceId = inputStream.readUTF();
                    String pointId = inputStream.readUTF();
                    System.out.println(pieceId);
                    System.out.println(pointId);
                }

                if (inputFlag == MESSAGE_TYPES.EXIT.getFlag()) {
                    room.removePlayerThread(this);
                    socket.close();
                    inputStream.close();
                    outputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendPlayableFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.PLAYABLE.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendWaitingFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.WAITING.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
