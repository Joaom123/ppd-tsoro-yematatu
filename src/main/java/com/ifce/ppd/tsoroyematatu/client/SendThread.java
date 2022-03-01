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

    }

    /**
     * Send INIT flag to server.
     *
     * @param roomId The room to be used.
     */
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

    /**
     * Send MESSAGE flag to server.
     *
     * @param message The message to be sent.
     */
    public void sendMessage(String message) {
        try {
            outputStream.writeByte(MESSAGE_TYPES.MESSAGE.getFlag());
            outputStream.writeUTF(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send MOVE flag to server.
     *
     * @param pieceId The pieceId to be sent.
     * @param pointId The pointId to be sent.
     */
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

    /**
     * Send EXIT flag to server.
     */
    public void sendExit() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.EXIT.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send WITHDRAWAL flag to server.
     */
    public void sendWithdrawalFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.WITHDRAWAL.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send DRAW flag to server.
     */
    public void sendDrawFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.DRAW.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send DRAW_DENIED flag to server.
     */
    public void sendDrawDeniedFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.DRAW_DENIED.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send DRAW_ACCEPTED flag to server.
     */
    public void sendDrawAcceptedFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.DRAW_ACCEPTED.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
