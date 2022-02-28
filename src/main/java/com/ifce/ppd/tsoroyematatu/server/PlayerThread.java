package com.ifce.ppd.tsoroyematatu.server;

import com.ifce.ppd.tsoroyematatu.constants.MESSAGE_TYPES;
import com.ifce.ppd.tsoroyematatu.exceptions.NoRivalException;
import com.ifce.ppd.tsoroyematatu.models.Client;

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
    private boolean isFirstPlayer = false;

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
        ObjectInputStream inputStream = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                byte inputFlag = inputStream.readByte();

                if (inputFlag == MESSAGE_TYPES.INIT.getFlag()) {
                    client = (Client) inputStream.readObject(); // set client
                    System.out.println("Cliente " + client.getName() + " inicializado no servidor");

                    String roomId = inputStream.readUTF();
                    room = server.createRoom(roomId); // set room
                    room.addPlayer(this); // add player to the room
                    System.out.println("Cliente " + client.getName() + " entrou na sala " + room.getId());

                    // If room isn't full, player should wait until another player enter.
                    // If full, create game and send playable flag.
                    if (!room.isFull()) {
                        sendWaitRivalConnectFlag();
                        sendIsFirstPlayerFlag();
                        isFirstPlayer = true;
                    } else {
                        room.createGame();
                        room.sendPlayable();
                    };
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
                    // If the move is valid, send move
                    if (room.getGame().isValidMove(pieceId, pointId)) {
                        room.sendMoveToPlayers(pieceId, pointId);
                        sendWaitRivalMakeMoveFlag();
                        room.getRivalPlayerThread(this).sendCanMakeMoveFlag();
                    }
                }

                if (inputFlag == MESSAGE_TYPES.EXIT.getFlag()) {
                    room.removePlayerThread(this);
                    socket.close();
                    inputStream.close();
                    outputStream.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
                this.interrupt();
                return;
            }
        }
    }

    private void sendIsFirstPlayerFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.IS_FIRST_PLAYER.getFlag());
        outputStream.flush();
    }

    private void sendWaitRivalMakeMoveFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.WAIT_RIVAL_MAKE_MOVE.getFlag());
        outputStream.flush();
    }

    public void sendPlayableFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.PLAYABLE.getFlag());
        outputStream.flush();
    }

    public void sendWaitRivalConnectFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.WAIT_RIVAL_CONNECT.getFlag());
        outputStream.flush();
    }

    public void sendCanMakeMoveFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.CAN_MAKE_MOVE.getFlag());
        outputStream.flush();
    }

    /**
     * Send a message to this plauerThread's client.
     *
     * @param author  The author of the message, can be the server or another player
     * @param message The message to be passed
     */
    public void sendMessage(String author, String message) throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.MESSAGE.getFlag());
        outputStream.writeUTF(author);
        outputStream.writeUTF(message);
        outputStream.flush();
    }

    /**
     * Send a message to the rival player in the room
     *
     * @param message The message to be sent.
     */
    public void sendMessageToRivalPlayer(String author, String message) throws NoRivalException, IOException {
        room.getRivalPlayerThread(this).sendMessage(author, message);
    }

    public void sendMoveToPlayer(String pieceId, String pointId) throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.MOVE.getFlag());
        outputStream.writeUTF(pieceId);
        outputStream.writeUTF(pointId);
        outputStream.writeInt(room.getGame().getTurn());
        outputStream.flush();
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void sendWinnerPlayerFlag(PlayerThread winnerPlayer) {
        try {
            if (winnerPlayer == this)
                outputStream.writeByte(MESSAGE_TYPES.WINNER.getFlag());
            else
                outputStream.writeByte(MESSAGE_TYPES.LOSER.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendDrawFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.DRAW.getFlag());
        outputStream.flush();
    }
}
