package com.ifce.ppd.tsoroyematatu;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This class is responsible to receive 'actions' from the server.
 * Allowing it to invoke remote objects.
 */
public interface ClientCallback extends Remote {
    /**
     * Execute the function goToGame
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void sendPlayable() throws RemoteException;

    /**
     * Receive the message from the server and send it to the player's chat.
     *
     * @param author  The message's author.
     * @param message The content of the message.
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void receiveMessage(String author, String message) throws RemoteException;

    /**
     * Move a piece to a point.
     *
     * @param pieceId The piece to be moved.
     * @param pointId The point of destiny of the piece.
     * @param turn    The current turn in the server.
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void move(String pieceId, String pointId, int turn) throws RemoteException;

    /**
     * Execute the function waitRivalMakeMove in current controller.
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void waitRivalMakeMove() throws RemoteException;

    /**
     * Execute the function canMakeMove in current controller.
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void canMakeMove() throws RemoteException;

    /**
     * Execute the function 'winner' in current controller.
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void winner() throws RemoteException;

    /**
     * Execute loser funtion in controller.
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void loser() throws RemoteException;

    /**
     * Execute resetGame in controller. Set turn to 0. Change firstPlayer.
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void resetGame() throws RemoteException;

    /**
     * Execute drawConfirmation in controller.
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void drawConfirmation() throws RemoteException;

    /**
     * Execute the function drawAccepted in current controller.
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void drawAccepted() throws RemoteException;

    /**
     * Execute the function 'drawDenied' in the current controller.
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void drawDenied() throws RemoteException;

    /**
     * Execute the function 'exit' in the current controller.
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void exit() throws RemoteException;

    /**
     * Execute the function 'roomIsFull' in current controller.
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void roomIsFull() throws RemoteException;

    /**
     * Execute the function 'ping' in the ServerConnection. Useful for debugging.
     *
     * @param message The message to be printed.
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void ping(String message) throws RemoteException;

    /**
     * Set first player flag in ServerConnection's object to true.
     *
     * @throws RemoteException When there is a failure to connect to the server.
     */
    void firstPlayer() throws RemoteException;
}
