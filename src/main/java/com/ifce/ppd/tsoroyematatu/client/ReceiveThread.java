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
        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                byte inputTypeFlag = inputStream.readByte();

                if (inputTypeFlag == MESSAGE_TYPES.MESSAGE.getFlag()) {
                    String author = inputStream.readUTF();
                    String message = inputStream.readUTF();
                    serverConnection.receiveMessage(author, message);
                }

                if (inputTypeFlag == MESSAGE_TYPES.MOVE.getFlag()) {
                    String pieceId = inputStream.readUTF();
                    String pointId = inputStream.readUTF();
                    int turn = inputStream.readInt();
                    serverConnection.setTurn(turn);
                    serverConnection.receiveMove(pieceId, pointId);
                }

                if (inputTypeFlag == MESSAGE_TYPES.PLAYABLE.getFlag())
                    serverConnection.goToGame();

                if (inputTypeFlag == MESSAGE_TYPES.IS_FIRST_PLAYER.getFlag())
                    serverConnection.setFirstPlayer(true);

                if (inputTypeFlag == MESSAGE_TYPES.WAIT_RIVAL_MAKE_MOVE.getFlag())
                    serverConnection.waitRivalMakeMove();

                if (inputTypeFlag == MESSAGE_TYPES.CAN_MAKE_MOVE.getFlag())
                    serverConnection.canMakeMove();

                if (inputTypeFlag == MESSAGE_TYPES.WINNER.getFlag())
                    serverConnection.winner();

                if (inputTypeFlag == MESSAGE_TYPES.LOSER.getFlag())
                    serverConnection.loser();

            } catch (Exception e) {
                e.printStackTrace();
                this.interrupt();
                return;
            }
        }
    }
}
