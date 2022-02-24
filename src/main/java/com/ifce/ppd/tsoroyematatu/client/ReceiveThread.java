package com.ifce.ppd.tsoroyematatu.client;

import com.ifce.ppd.tsoroyematatu.constants.MESSAGE_TYPES;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * This thread is responsible for reading user's input and send it to the server.
 */
public class ReceiveThread extends Thread {
    private final ServerConnection serverConnection;
    private final Socket socket;
    private ObjectInputStream inputStream;

    public ReceiveThread(Socket socket, ServerConnection serverConnection) {
        this.socket = socket;
        this.serverConnection = serverConnection;

        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte inputTypeFlag;
        while (true) {
            try {
                inputTypeFlag = inputStream.readByte();

                if (inputTypeFlag == MESSAGE_TYPES.MESSAGE.getFlag()) {
                    String author = inputStream.readUTF();
                    String message = inputStream.readUTF();
                    this.serverConnection.receiveMessage(author, message);
                }

                if (inputTypeFlag == MESSAGE_TYPES.MOVE.getFlag()) {

                }

                if (inputTypeFlag == MESSAGE_TYPES.WAITING.getFlag()) {
                }

                if (inputTypeFlag == MESSAGE_TYPES.PLAYABLE.getFlag()) {
                    serverConnection.goToGame();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
