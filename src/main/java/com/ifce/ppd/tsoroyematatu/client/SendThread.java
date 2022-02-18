package com.ifce.ppd.tsoroyematatu.client;

import com.ifce.ppd.tsoroyematatu.constants.MESSAGE_TYPES;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SendThread extends Thread {
    private final Socket socket;
    private final ServerConnection serverConnection;
    private ObjectOutputStream outputStream;

    public SendThread(Socket socket, ServerConnection serverConnection) {
        this.socket = socket;
        this.serverConnection = serverConnection;

        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        // TODO: close outputStream when user disconnect
    }

    public void sendInitFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.INIT.getFlag());
            outputStream.writeObject(serverConnection.getClientModel());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try {
            outputStream.writeByte(MESSAGE_TYPES.MESSAGE.getFlag());
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
