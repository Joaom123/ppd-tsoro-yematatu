package com.ifce.ppd.tsoroyematatu.client;

import com.ifce.ppd.tsoroyematatu.constants.MESSAGE_TYPES;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * This thread is responsible for reading user's input and send it to the server.
 */
public class SendThread extends Thread {
    private final ServerConnection serverConnection;
    private ObjectOutputStream outputStream;

    public SendThread(Socket socket, ServerConnection serverConnection) {
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

    public void sendInitFlag(String roomId) {
        try {
            outputStream.writeByte(MESSAGE_TYPES.INIT.getFlag());
            outputStream.writeObject(serverConnection.getClient());
            outputStream.writeUTF(roomId);
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

    public void sendMove(String pieceId, String pointId) {
        try {
            outputStream.writeByte(MESSAGE_TYPES.MOVE.getFlag());
            outputStream.writeUTF(pieceId);
            outputStream.writeUTF(pointId);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendExit() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.EXIT.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendWithdrawalFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.WITHDRAWAL.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDrawFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.DRAW.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDrawDeniedFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.DRAW_DENIED.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDrawAcceptedFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.DRAW_ACCEPTED.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
