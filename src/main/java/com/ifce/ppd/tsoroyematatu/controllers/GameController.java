package com.ifce.ppd.tsoroyematatu.controllers;

import com.ifce.ppd.tsoroyematatu.client.ServerConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class GameController extends Controller implements Initializable {
    private final ServerConnection serverConnection;

    @FXML
    private TextField chatInput;

    @FXML
    private TextArea chatMessages;

    @FXML
    private Circle selectedPiece;

    @FXML
    private GridPane gameGrid;

    @FXML
    public Text playerName;

    @FXML
    public Text host;

    @FXML
    public Text roomId;

    @FXML
    public Text turn;

    private Set<Circle> pieces = new HashSet<>();
    private Set<Circle> rivalPieces = new HashSet<>();

    public GameController(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        this.serverConnection.setCurrentController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerName.setText(serverConnection.getClient().getName());
        host.setText(serverConnection.getHostname());
        roomId.setText(serverConnection.getRoomId());
        turn.setText(String.valueOf(serverConnection.getTurn()));

        if (this.serverConnection.isFirstPlayer()) {
            pieces.add(getElementById("piece-first-1"));
            pieces.add(getElementById("piece-first-2"));
            pieces.add(getElementById("piece-first-3"));
            rivalPieces.add(getElementById("piece-second-1"));
            rivalPieces.add(getElementById("piece-second-2"));
            rivalPieces.add(getElementById("piece-second-3"));
        } else {
            pieces.add(getElementById("piece-second-1"));
            pieces.add(getElementById("piece-second-2"));
            pieces.add(getElementById("piece-second-3"));
            rivalPieces.add(getElementById("piece-first-1"));
            rivalPieces.add(getElementById("piece-first-2"));
            rivalPieces.add(getElementById("piece-first-3"));


            int index = 0;
            for (Circle c : rivalPieces) {
                setRowAndColumnElement(c, 1, 1 + index);
                index++;
            }

            index = 0;
            for (Circle c : pieces) {
                setRowAndColumnElement(c, 11, 1 + index);
                index++;
            }
        }

        for (Circle c : rivalPieces)
            c.setDisable(true);
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
            System.out.println(e.getMessage());
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
    }

    public void movePieceToPoint(Circle selectedPiece, Circle clickedPoint) {
        int clickedPointRow = GridPane.getRowIndex(clickedPoint);
        int clickedPointColumn = GridPane.getColumnIndex(clickedPoint);
        setRowAndColumnElement(selectedPiece, clickedPointRow, clickedPointColumn);
    }

    private void setRowAndColumnElement(Circle selectedPiece, int rowIndex, int columnIndex) {
        GridPane.setRowIndex(selectedPiece, rowIndex);
        GridPane.setColumnIndex(selectedPiece, columnIndex);
    }

    @Override
    public void receiveMove(String selectedPieceId, String selectedPointId) {
        Circle piece = getElementById(selectedPieceId);
        Circle point = getElementById(selectedPointId);

        movePieceToPoint(piece, point);
        point.setVisible(false);
        selectedPiece = null;
    }

    private Circle getElementById(String id) {
        return (Circle) gameGrid.lookup("#"+ id);
    }

    @Override
    public void waitRivalMakeMove() {
        for (Circle c1 : pieces)
            c1.setDisable(true);
    }
}