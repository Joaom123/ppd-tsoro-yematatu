package com.ifce.ppd.tsoroyematatu.client;

import com.ifce.ppd.tsoroyematatu.controllers.Controller;
import com.ifce.ppd.tsoroyematatu.exceptions.NoClientModelException;
import com.ifce.ppd.tsoroyematatu.models.Client;

import java.io.IOException;
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

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    /**
     * Start the connection with the server with the following configuration:
     * Host: localhost
     * Port: 12345
     * @throws IOException
     */
    public void startConnection() {
        try {
            socket = new Socket("localhost", 12345);

            receiveThread = new ReceiveThread(socket, this);
            sendThread = new SendThread(socket, this);

            receiveThread.start();
            sendThread.start();

            isConnected = true;
        } catch (IOException e) {
            isConnected = false;
            e.printStackTrace();
        }
    }

    public void createClientOnServer() throws NoClientModelException {
        if (clientModel == null) throw new NoClientModelException();
        sendThread.sendInitFlag();
    }

    public Socket getSocket() {
        return socket;
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

    public void sendMessage(String inputText) {
        this.sendThread.sendMessage(inputText);
    }

    public void receiveMessage(String message, Client client) {
        this.currentController.addMessageToChat("From server" + client.getName(), message);
    }
}
