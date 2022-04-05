package com.ifce.ppd.tsoroyematatu;

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
                assert inputStream != null;
                byte inputFlag = inputStream.readByte();

                if (inputFlag == MESSAGE_TYPES.INIT.getFlag()) {
                    client = (Client) inputStream.readObject(); // set client
                    System.out.println("Cliente " + client.getName() + " inicializado no servidor");

                    String roomId = inputStream.readUTF();
                    room = server.createRoom(roomId); // set room
                    try {
                        room.addPlayer(this); // add player to the room
                    } catch (MaximumNumberPlayersInTheRoomException e) {
                        room = null; // remove player from the room
                        sendRoomIsFullFlag();
                    }
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
                    }
                }

                if (inputFlag == MESSAGE_TYPES.MESSAGE.getFlag()) {
                    String message = inputStream.readUTF();
                    sendMessageToRivalPlayer(client.getName(), message);
                }

                if (inputFlag == MESSAGE_TYPES.MOVE.getFlag()) {
                    String pieceId = inputStream.readUTF();
                    String pointId = inputStream.readUTF();

                    // If the move is valid, send move
                    if (room.getGame().isValidMove(pieceId, pointId)) {
                        room.sendMoveToPlayers(pieceId, pointId);
                        sendWaitRivalMakeMoveFlag();
                        room.getRivalPlayerThread(this).sendCanMakeMoveFlag();
                    }
                }

                if (inputFlag == MESSAGE_TYPES.EXIT.getFlag()) {
                    if (!room.isFull()) {
                        sendExitFlag();

                    } else {
                        sendExitFlag();
                        room.getRivalPlayerThread(this).sendExitFlag();
                    }

                    room.removePlayerThread(this);
                    socket.close();
                    inputStream.close();
                    outputStream.close();
                }

                if (inputFlag == MESSAGE_TYPES.WITHDRAWAL.getFlag()) {
                    PlayerThread rivalPlayer = room.getRivalPlayerThread(this);
                    sendWinnerPlayerFlag(rivalPlayer);
                    rivalPlayer.sendWinnerPlayerFlag(rivalPlayer);
                    room.resetGame();
                }

                if (inputFlag == MESSAGE_TYPES.DRAW.getFlag())
                    room.getRivalPlayerThread(this).sendDrawConfirmationFlag();

                if (inputFlag == MESSAGE_TYPES.DRAW_ACCEPTED.getFlag())
                    room.sendDraw();

                if (inputFlag == MESSAGE_TYPES.DRAW_DENIED.getFlag())
                    room.getRivalPlayerThread(this).sendDrawDeniedFlag();

            } catch (IOException e) {
                e.printStackTrace();
                interrupt();
                return;
            } catch (NoRivalException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Send ROOM_IS_FULL flag.
     *
     * @throws IOException IOException
     */
    private void sendRoomIsFullFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.ROOM_IS_FULL.getFlag());
        outputStream.flush();
    }

    /**
     * Send DRAW_DENIED flag.
     *
     * @throws IOException IOException
     */
    private void sendDrawDeniedFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.DRAW_DENIED.getFlag());
        outputStream.flush();
    }

    /**
     * Send IS_FIRST_PLAYER flag.
     *
     * @throws IOException IOException
     */
    private void sendIsFirstPlayerFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.IS_FIRST_PLAYER.getFlag());
        outputStream.flush();
    }

    /**
     * Send WAIT_RIVAL_MAKE_MOVE flag.
     *
     * @throws IOException IOException
     */
    private void sendWaitRivalMakeMoveFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.WAIT_RIVAL_MAKE_MOVE.getFlag());
        outputStream.flush();
    }

    /**
     * Send PLAYABLE flag.
     *
     * @throws IOException IOException
     */
    public void sendPlayableFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.PLAYABLE.getFlag());
        outputStream.flush();
    }

    /**
     * Send WAIT_RIVAL_CONNECT flag.
     *
     * @throws IOException IOException
     */
    public void sendWaitRivalConnectFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.WAIT_RIVAL_CONNECT.getFlag());
        outputStream.flush();
    }

    /**
     * Send CAN_MAKE_MOVE flag.
     *
     * @throws IOException IOException
     */
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

    /**
     * Send MOVE flag. Send the pieceId, pointId and room's turn.
     *
     * @throws IOException IOException
     */
    public void sendMoveToPlayer(String pieceId, String pointId) throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.MOVE.getFlag());
        outputStream.writeUTF(pieceId);
        outputStream.writeUTF(pointId);
        outputStream.writeInt(room.getGame().getTurn());
        outputStream.flush();
    }

    /**
     * FirstPlayer's getter.
     *
     * @return FirstPlayer flag.
     */
    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    /**
     * FirstPlayer's setter.
     *
     * @param firstPlayer First player flag to be set.
     */
    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    /**
     * Send WINNER or LOSER flag.
     */
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

    /**
     * Send DRAW_ACCEPTED flag.
     *
     * @throws IOException IOException
     */
    public void sendDrawFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.DRAW_ACCEPTED.getFlag());
        outputStream.flush();
    }

    /**
     * Send DRAW_CONFIRMATION flag.
     *
     * @throws IOException IOException
     */
    public void sendDrawConfirmationFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.DRAW_CONFIRMATION.getFlag());
        outputStream.flush();
    }

    /**
     * Send RESET_GAME flag.
     */
    public void sendResetFlag() {
        try {
            outputStream.writeByte(MESSAGE_TYPES.RESET_GAME.getFlag());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send EXIT flag.
     *
     * @throws IOException IOException
     */
    public void sendExitFlag() throws IOException {
        outputStream.writeByte(MESSAGE_TYPES.EXIT.getFlag());
        outputStream.flush();
    }
}
