package com.ifce.ppd.tsoroyematatu.controllers;

import com.ifce.ppd.tsoroyematatu.client.ServerConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GameController implements Controller{
    public TextField chatInput;
    public TextArea chatMessages;
    private ServerConnection serverConnection;

    public GameController(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        this.serverConnection.setCurrentController(this);
    }

    /**
     *
     * @param actionEvent
     */
    public void handleChatInput(ActionEvent actionEvent) {
        String inputText = chatInput.getText();

        // If user typed nothing, exit function
        if (inputText.equals("")) return;

        chatInput.setText("");

        addMessageToChat(serverConnection.getClientModel().getName(), inputText);

        //Send inputText to server
        try {
            serverConnection.sendMessage(inputText);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addMessageToChat(String author, String message) {
        chatMessages.appendText(author + ": " + message + "\n");
    }
}