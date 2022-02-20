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
    public Controller currentController;
    private Socket socket;
    private Client clientModel;
    private SendThread sendThread;
    private ReceiveThread receiveThread;
    private boolean isConnected = false;

    public boolean isConnected() {
        return isConnected;
    }

    /**
     * Start the connection with the server (port 12345) and start ReceiveThread and SendThread.
     * If the connection cannot be done, set isConnected to false.
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
     *
     * @throws NullClientException Lançada quando não há cliente.
     */
    public void createClientOnServer(String roomId) throws NullClientException {
        if (clientModel == null) throw new NullClientException();
        sendThread.sendInitFlag(roomId);
    }

    public Client getClientModel() {
        return clientModel;
    }

    public void setClientModel(Client clientModel) {
        this.clientModel = clientModel;
    }

    public void setCurrentController(Controller currentController) {
        this.currentController = currentController;
    }

    /**
     * Use the sendThread to send message (MESSAGE_TYPES.MESSAGE) to server.
     * @param inputText The content of the message.
     */
    public void sendMessage(String inputText) {
        this.sendThread.sendMessage(inputText);
    }

    /**
     * Used by the ReceiveThread to get the message from the server and send it to the chat.
     * @param message The content of the message.
     * @param client The message's author.
     */
    public void receiveMessage(String message, Client client) {
        this.currentController.addMessageToChat(client.getName(), message);
    }
}
