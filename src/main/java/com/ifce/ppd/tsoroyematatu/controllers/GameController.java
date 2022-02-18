package com.ifce.ppd.tsoroyematatu.controllers;

import com.ifce.ppd.tsoroyematatu.services.ServerConnection;
import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class GameController {

    public TextField chatInput;
    public TextArea chatMessages;
    private final ServerConnection serverConnection;

    public GameController(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * @param actionEvent
     */
    public void handleChatInput(ActionEvent actionEvent) {
        String inputText = chatInput.getText();
        chatInput.setText("");

        chatMessages.appendText(serverConnection.getClientModel().getName() + ": " + inputText + "\n");

        //Send inputText to server
        try {
            serverConnection.sendMessage(inputText);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}