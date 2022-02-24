package com.ifce.ppd.tsoroyematatu.controllers;

import com.ifce.ppd.tsoroyematatu.client.ServerConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

public class GameController extends Controller {
    private final ServerConnection serverConnection;

    @FXML
    private TextField chatInput;

    @FXML
    private TextArea chatMessages;

    @FXML
    private Circle pieceB1;

    @FXML
    private Circle selectedPiece;

    @FXML
    private GridPane gameGrid;

    public GameController(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        this.serverConnection.setCurrentController(this);
    }

    /**
     * @param actionEvent The action's event
     */
    public void handleChatInput(ActionEvent actionEvent) {
        String inputText = chatInput.getText();

        // If user typed nothing, exit function
        if (inputText.equals("")) return;

        chatInput.setText("");

        addMessageToChat(serverConnection.getClient().getName(), inputText);

        // Send inputText to server
        try {
            serverConnection.sendMessage(inputText);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addMessageToChat(String author, String message) {
        chatMessages.appendText(author + ": " + message + "\n");
    }

    public void handleClickOnPiece(MouseEvent mouseEvent) {
        selectedPiece = (Circle) mouseEvent.getTarget();
    }

    public void handleClickOnPoint(MouseEvent mouseEvent) {
        if (selectedPiece == null) return;

        Circle clickedPoint = (Circle) mouseEvent.getTarget();
        serverConnection.sendMove(selectedPiece.getId(), clickedPoint.getId());
        movePieceToPoint(selectedPiece, clickedPoint);
        clickedPoint.setVisible(false);
        selectedPiece = null;
    }

    public void movePieceToPoint(Circle selectedPiece, Circle clickedPoint) {
        int clickedPointRow = GridPane.getRowIndex(clickedPoint);
        int clickedPointColumn = GridPane.getColumnIndex(clickedPoint);

        GridPane.setRowIndex(selectedPiece, clickedPointRow);
        GridPane.setColumnIndex(selectedPiece, clickedPointColumn);
    }
}