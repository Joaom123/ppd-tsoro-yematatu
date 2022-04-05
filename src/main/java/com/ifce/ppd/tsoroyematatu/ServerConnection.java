package com.ifce.ppd.tsoroyematatu;

import java.net.Socket;

/**
 * Start the client, connects to a server with a hostname/ipAdress and port.
 * Once the connection is made, creates and starts two threads: SendThread and ReceiveThread.
 */
public class ServerConnection {
    private Controller currentController;
    private Client clientModel;
    private String roomId;
    private String hostname;
    private SendThread sendThread;
    private boolean isConnected = false;
    private boolean isFirstPlayer = false;
    private int turn = 0;

    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Start the connection with the server (port 12345) and start ReceiveThread and SendThread.
     * If the connection cannot be done, set isConnected to false.
     *
     * @param host The hostname ex.: localhost | 127.0.0.1
     */
    public void startConnection(String host) {
        try {
            Socket socket = new Socket(host, 12345);

            ReceiveThread receiveThread = new ReceiveThread(socket, this);
            sendThread = new SendThread(socket, this);

            receiveThread.start();
            sendThread.start();

            isConnected = true;
        } catch (Exception e) {
            isConnected = false;
            e.printStackTrace();
        }
    }

    /**
     * Send init flag.
     *
     * @throws NullClientException Lançada quando não há cliente.
     */
    public void createClientOnServer(String roomId) throws NullClientException {
        if (clientModel == null) throw new NullClientException();
        sendThread.sendInitFlag(roomId);
    }

    /**
     * Client's getter.
     *
     * @return The Client.
     */
    public Client getClient() {
        return clientModel;
    }

    /**
     * Client's Setter.
     *
     * @param clientModel Client to be set.
     */
    public void setClient(Client clientModel) {
        this.clientModel = clientModel;
    }

    /**
     * FirstPlayer's getter.
     *
     * @return FirstPlayer
     */
    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    /**
     * FirstPlayer's setter.
     *
     * @param firstPlayer FirstPlayer to be set.
     */
    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    /**
     * RoomId's getter.
     *
     * @return RoomId.
     */
    public String getRoomId() {
        return roomId;
    }

    /**
     * RoomId's setter.
     *
     * @param roomId RoomId to be set.
     */
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    /**
     * Hostname's getter.
     *
     * @return Hostname.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Hostname's setter.
     *
     * @param hostname Hostname to be set.
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Turn's getter.
     *
     * @return Turn number.
     */
    public int getTurn() {
        return turn;
    }

    /**
     * Setter of turn. Update the turn on the controller.
     *
     * @param turn Turn number.
     */
    public void setTurn(int turn) {
        this.turn = turn;
        currentController.updateTurn();
    }

    /**
     * CurrectController's setter.
     *
     * @param currentController The controller to be set.
     */
    public void setCurrentController(Controller currentController) {
        this.currentController = currentController;
    }

    /**
     * Use the sendThread to send message to server.
     *
     * @param inputText The content of the message.
     */
    public void sendMessage(String inputText) {
        sendThread.sendMessage(inputText);
    }

    /**
     * Used by the ReceiveThread to get the message from the server and send it to the chat.
     *
     * @param author  The message's author.
     * @param message The content of the message.
     */
    public void receiveMessage(String author, String message) {
        currentController.addMessageToChat(author, message);
    }

    /**
     * Use the sendThread to send the move to the server.
     *
     * @param pieceId The pieceId
     * @param pointId The pointId
     */
    public void sendMove(String pieceId, String pointId) {
        sendThread.sendMove(pieceId, pointId);
    }

    /**
     * Use the receiveThread to get the move valitation, and pass it to the current controller.
     *
     * @param pieceId The piece's id
     * @param pointId The point's id
     */
    public void receiveMove(String pieceId, String pointId) {
        currentController.receiveMove(pieceId, pointId);
    }

    /**
     * Execute goToGame in controller.
     */
    public void goToGame() {
        currentController.goToGame();
    }

    /**
     * Send exit flag.
     */
    public void sendExit() {
        sendThread.sendExit();
    }

    /**
     * Execute waitRivalMakeMove in controller.
     */
    public void waitRivalMakeMove() {
        currentController.waitRivalMakeMove();
    }

    /**
     * Execute canMakeMove in controller.
     */
    public void canMakeMove() {
        currentController.canMakeMove();
    }

    /**
     * Execute winner fuction in controller.
     */
    public void winner() {
        System.out.println("Venceu");
        currentController.winner();
    }

    /**
     * Execute loser funtion in controller.
     */
    public void loser() {
        System.out.println("Perdeu");
        currentController.loser();
    }

    /**
     * Send withdrawal flag.
     */
    public void sendWithdrawalFlag() {
        sendThread.sendWithdrawalFlag();
    }

    /**
     * Execute resetGame in controller. Set turn to 0. Change firstPlayer.
     */
    public void resetGame() {
        turn = 0;
        isFirstPlayer = !isFirstPlayer;
        currentController.resetGame();
    }

    /**
     * Send draw flag.
     */
    public void sendDrawFlag() {
        sendThread.sendDrawFlag();
    }

    /**
     * Execute drawConfirmation in controller.
     */
    public void drawConfirmation() {
        currentController.drawConfirmation();
    }

    /**
     * Send draw denied flag.
     */
    public void sendDrawDeniedFlag() {
        sendThread.sendDrawDeniedFlag();
    }

    /**
     * Send draw accepted flag.
     */
    public void sendDrawAcceptedFlag() {
        sendThread.sendDrawAcceptedFlag();
    }

    /**
     * Execute drawAccpeted in controller.
     */
    public void drawAccepted() {
        currentController.drawAccepted();
    }

    /**
     * Execute drawDenied in controller.
     */
    public void drawDenied() {
        currentController.drawDenied();
    }

    /**
     * Execute exit in controller.
     */
    public void exit() {
        // destroy connection
        currentController.exit();
    }

    /**
     * Execute roomIsFull in controller.
     */
    public void roomIsFull() {
        currentController.roomIsFull();
    }
}
