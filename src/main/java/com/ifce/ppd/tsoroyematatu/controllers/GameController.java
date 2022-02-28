package com.ifce.ppd.tsoroyematatu.controllers;

import com.ifce.ppd.tsoroyematatu.client.ServerConnection;
import com.ifce.ppd.tsoroyematatu.models.PieceFront;
import com.ifce.ppd.tsoroyematatu.services.JavaFXService;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    public Text playerName;
    @FXML
    public Text host;
    @FXML
    public Text roomId;
    @FXML
    public Text turn;
    @FXML
    private TextField chatInput;
    @FXML
    private TextArea chatMessages;
    @FXML
    private Circle selectedPiece;
    @FXML
    private GridPane gameGrid;
    private Set<PieceFront> pieces;
    private Set<PieceFront> rivalPieces;
    private Set<Circle> points = new HashSet<>();
    private final JavaFXService javaFXService = new JavaFXService();

    public GameController(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        this.serverConnection.setCurrentController(this);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerName.setText(serverConnection.getClient().getName());
        host.setText(serverConnection.getHostname());
        roomId.setText(serverConnection.getRoomId());
        for (int i = 0; i < 7; i++) points.add(getElementById("point-"+i));
        updateTurn();
        initGame();
    }

    /**
     * @param actionEvent The action's event
     */
    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    public void handleClickOnPiece(MouseEvent mouseEvent) {
        selectedPiece = (Circle) mouseEvent.getTarget();
    }

    @SuppressWarnings("unused")
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

        PieceFront pf = getPieceFront(piece);
        if (pf == null) return;

        Circle originPoint = pf.getPoint();

        pf.setPoint(point); // set destiny point

        movePieceToPoint(piece, point);
        point.setVisible(false);
        selectedPiece = null;
        if (originPoint != null) originPoint.setVisible(true);
        if (serverConnection.getTurn() < 6) piece.setDisable(true);
    }

    private Circle getElementById(String id) {
        return (Circle) gameGrid.lookup("#" + id);
    }

    private PieceFront getPieceFront(Circle pieceC) {
        for (PieceFront pf : pieces)
            if (pf.getPiece() == pieceC)
                return pf;
        for (PieceFront pf : rivalPieces)
            if (pf.getPiece() == pieceC)
                return pf;
        return null;
    }

    @Override
    public void waitRivalMakeMove() {
        for (PieceFront pf : pieces)
            pf.getPiece().setDisable(true);
    }

    @Override
    public void canMakeMove() { //executada após update turn
        for (PieceFront pf : rivalPieces) pf.getPiece().setDisable(true);
        for (PieceFront pf : pieces) pf.getPiece().setDisable(false);

        int turnNumber = serverConnection.getTurn();
        if (turnNumber > 1 && turnNumber < 6) {
            for (PieceFront pf : pieces)
                if (pf.getPoint() != null) pf.getPiece().setDisable(true);
        }
    }

    @Override
    public void updateTurn() {
        int turnNumber = serverConnection.getTurn();
        turn.setText(String.valueOf(turnNumber));
    }

    @FXML @SuppressWarnings("unused")
    public void handleDrawButtonClick(ActionEvent actionEvent) {
    }

    @FXML @SuppressWarnings("unused")
    public void handleGiveUpButtonClick(ActionEvent actionEvent) {
        serverConnection.sendWithdrawalFlag();
    }

    @FXML @SuppressWarnings("unused")
    public void handleExitButtonClick(ActionEvent actionEvent) {
    }

    @Override
    public void loser() {
        Platform.runLater(() -> {
            javaFXService.infoAlert("Você perdeu!", "Jogue novamente para tentar vencer!");
            for (PieceFront pf : pieces) pf.getPiece().setDisable(true);
            for (PieceFront pf : rivalPieces) pf.getPiece().setDisable(true);
        });
    }

    @Override
    public void winner() {
        Platform.runLater(() -> {
            javaFXService.infoAlert("Você ganhou!", "Parabéns, você ganhou. Jogue novamente para tentar vencer!");
            setDisableToAllPieces(true);
        });
    }

    private void setDisableToAllPieces(boolean isDisabled) {
        for (PieceFront pf : pieces) pf.getPiece().setDisable(isDisabled);
        for (PieceFront pf : rivalPieces) pf.getPiece().setDisable(isDisabled);
    }

    @Override
    public void resetGame() {
        System.out.println("Game reiniciado!");
        Platform.runLater(() -> {
            updateTurn();
            initGame();
            for (Circle p : points)
                p.setVisible(true);
        });
    }

    private void initGame() {
        pieces = new HashSet<>();
        rivalPieces = new HashSet<>();

        if (this.serverConnection.isFirstPlayer()) {
            pieces.add(new PieceFront(getElementById("piece-first-1")));
            pieces.add(new PieceFront(getElementById("piece-first-2")));
            pieces.add(new PieceFront(getElementById("piece-first-3")));
            rivalPieces.add(new PieceFront(getElementById("piece-second-1")));
            rivalPieces.add(new PieceFront(getElementById("piece-second-2")));
            rivalPieces.add(new PieceFront(getElementById("piece-second-3")));
            javaFXService.infoAlert(null, "Você é o primeiro jogador! Pode fazer a sua jogada.");
            setPiecesOnBoard();
            for (PieceFront pf : rivalPieces) pf.getPiece().setDisable(true);
            for (PieceFront pf : pieces) pf.getPiece().setDisable(false);
        } else {
            pieces.add(new PieceFront(getElementById("piece-second-1")));
            pieces.add(new PieceFront(getElementById("piece-second-2")));
            pieces.add(new PieceFront(getElementById("piece-second-3")));
            rivalPieces.add(new PieceFront(getElementById("piece-first-1")));
            rivalPieces.add(new PieceFront(getElementById("piece-first-2")));
            rivalPieces.add(new PieceFront(getElementById("piece-first-3")));

            setPiecesOnBoard();
            setDisableToAllPieces(true);
        }
    }

    private void setPiecesOnBoard() {
        int index = 0;
        for (PieceFront pf : rivalPieces) {
            setRowAndColumnElement(pf.getPiece(), 1, 2 + index);
            index++;
        }

        index = 0;
        for (PieceFront pf : pieces) {
            setRowAndColumnElement(pf.getPiece(), 11, 2 + index);
            index++;
        }
    }
}