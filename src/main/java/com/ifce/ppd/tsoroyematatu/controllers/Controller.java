package com.ifce.ppd.tsoroyematatu.controllers;

public abstract class Controller {
    /**
     * Add a message into the chat.
     *
     * @param author  The message's author.
     * @param message The content of the message.
     */
    public void addMessageToChat(String author, String message) {}

    /**
     * Change the scene to the game one, with the board and chat.
     */
    public void goToGame() {}

    /**
     * Receive a move from the serve and replicate it in the UI.
     *
     * @param selectedPieceId The piece's id.
     * @param selectedPointId The point's id.
     */
    public void receiveMove(String selectedPieceId, String selectedPointId) {}
}
