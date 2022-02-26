package com.ifce.ppd.tsoroyematatu.client;

import com.ifce.ppd.tsoroyematatu.controllers.Controller;
import com.ifce.ppd.tsoroyematatu.exceptions.NullClientException;
import com.ifce.ppd.tsoroyematatu.models.Client;

import java.net.Socket;

/**
 * Start the client, connects to a server with a hostname/ipAdress and port.
 * Once the connection is made, creates and starts two threads: SendThread and ReceiveThread.
 */
public class ServerConnection {
    private Controller currentController;
    private Socket socket;
    private Client clientModel;
    private String roomId;
    private String hostname;
    private SendThread sendThread;
    private ReceiveThread receiveThread;
    private boolean isConnected = false;
    private boolean isPlayable = false;
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
            socket = new Socket(host, 12345);

            receiveThread = new ReceiveThread(socket, this);
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
     * @throws NullClientException Lançada quando não há cliente.
     */
    public void createClientOnServer(String roomId) throws NullClientException {
        if (clientModel == null) throw new NullClientException();
        sendThread.sendInitFlag(roomId);
    }

    public Client getClient() {
        return clientModel;
    }

    public void setClient(Client clientModel) {
        this.clientModel = clientModel;
    }

    public boolean isFirstPlayer() {
        return isFirstPlayer;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        isFirstPlayer = firstPlayer;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
        currentController.updateTurn();
    }

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

    public void goToGame() {
        isPlayable = true;
        currentController.goToGame();
    }

    public void sendExit() {
        sendThread.sendExit();
    }

    public void waitRivalMakeMove() {
        currentController.waitRivalMakeMove();
    }

    public void canMakeMove() {
        currentController.canMakeMove();
    }
}
