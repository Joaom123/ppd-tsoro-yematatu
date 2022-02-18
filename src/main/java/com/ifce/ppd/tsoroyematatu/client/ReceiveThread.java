package com.ifce.ppd.tsoroyematatu.client;

import com.ifce.ppd.tsoroyematatu.constants.MESSAGE_TYPES;
import com.ifce.ppd.tsoroyematatu.models.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * This thread is responsible for reading user's input and send it to the server.
 */
public class ReceiveThread extends Thread {
    private ObjectInputStream inputStream;
    private final ServerConnection serverConnection;
    private final Socket socket;

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
        while (true) {
            try {
                if (inputStream.readByte() == MESSAGE_TYPES.MESSAGE.getFlag()) {
                    String message = inputStream.readUTF();
                    Client client = (Client) inputStream.readObject();
                    this.serverConnection.receiveMessage(message, client);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
