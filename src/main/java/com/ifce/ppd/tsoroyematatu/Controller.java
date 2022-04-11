package com.ifce.ppd.tsoroyematatu;

public abstract class Controller {
    /**
     * Add a message into the chat.
     *
     * @param author  The message's author.
     * @param message The content of the message.
     */
    public void addMessageToChat(String author, String message) {
    }

    /**
     * Change the scene to the game one, with the board and chat.
     */
    public void goToGame() {
        System.out.println("void");
    }

    /**
     * Receive a move from the serve and replicate it in the UI.
     *
     * @param selectedPieceId The piece's id.
     * @param selectedPointId The point's id.
     */
    public void receiveMove(String selectedPieceId, String selectedPointId) {
    }

    /**
     * Disable pieces of the player. So the player must wait to rival make move.
     */
    public void waitRivalMakeMove() {
    }

    /**
     * After the rival makes his move, the player is released to make his move.
     */
    public void canMakeMove() {
    }

    /**
     * Update turn number.
     */
    public void updateTurn() {
    }

    /**
     * Show loser message.
     */
    public void loser() {
    }

    /**
     * Show winner message.
     */
    public void winner() {
    }

    /**
     * Reset the game's turn, pieces and points.
     */
    public void resetGame() {
    }

    /**
     * Show draw confirmation dialog with options 'Yes' and 'No'.
     */
    public void drawConfirmation() {
    }

    /**
     * Show dialog with accpeted draw message.
     */
    public void drawAccepted() {
    }

    /**
     * Show dialog with denied draw message.
     */
    public void drawDenied() {
    }

    /**
     * Exit the game. Go to init view.
     */
    public void exit() {
    }

    /**
     * Show 'room is full' dialog.
     */
    public void roomIsFull() {
    }
}
