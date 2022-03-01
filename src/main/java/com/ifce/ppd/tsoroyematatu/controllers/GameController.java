package com.ifce.ppd.tsoroyematatu.controllers;

import com.ifce.ppd.tsoroyematatu.client.ServerConnection;
import com.ifce.ppd.tsoroyematatu.models.PieceFront;
import com.ifce.ppd.tsoroyematatu.services.JavaFXService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
    private final Set<Circle> points = new HashSet<>();
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
     * Handles the input's event of chat.
     *
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

    /**
     * Add message to chat.
     *
     * @param author  The message's author.
     * @param message The content of the message.
     */
    public void addMessageToChat(String author, String message) {
        chatMessages.appendText(author + ": " + message + "\n");
    }

    /**
     * Handles the mouse's event of Piece.
     *
     * @param mouseEvent The action's event
     */
    @SuppressWarnings("unused")
    public void handleClickOnPiece(MouseEvent mouseEvent) {
        selectedPiece = (Circle) mouseEvent.getTarget();
    }

    /**
     * Handles the mouse's event of Point.
     *
     * @param mouseEvent The action's event
     */
    @SuppressWarnings("unused")
    public void handleClickOnPoint(MouseEvent mouseEvent) {
        if (selectedPiece == null) return;

        Circle clickedPoint = (Circle) mouseEvent.getTarget();
        serverConnection.sendMove(selectedPiece.getId(), clickedPoint.getId());
    }

    /**
     * Move piece to given point location.
     *
     * @param selectedPiece The selected piece.
     * @param clickedPoint The selected point.
     */
    public void movePieceToPoint(Circle selectedPiece, Circle clickedPoint) {
        int clickedPointRow = GridPane.getRowIndex(clickedPoint);
        int clickedPointColumn = GridPane.getColumnIndex(clickedPoint);
        setRowAndColumnElement(selectedPiece, clickedPointRow, clickedPointColumn);
    }

    /**
     * Set the row and column of an element in the GridPane.
     *
     * @param selectedPiece The element.
     * @param rowIndex The row index to be set.
     * @param columnIndex The column index to be set.
     */
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

    /**
     * Get element by id.
     * @param id The id of an element.
     * @return The element.
     */
    private Circle getElementById(String id) {
        return (Circle) gameGrid.lookup("#" + id);
    }

    /**
     * Get PieceFront for given element.
     *
     * @param pieceC Given element.
     * @return The PieceFront of that element.
     */
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
    public void canMakeMove() {
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

    /**
     * Handles the click's event of Draw button.
     *
     * @param actionEvent The action's event
     */
    @FXML @SuppressWarnings("unused")
    public void handleDrawButtonClick(ActionEvent actionEvent) {
        serverConnection.sendDrawFlag();
    }

    /**
     * Handles the click's event of Give up button.
     *
     * @param actionEvent The action's event
     */
    @FXML @SuppressWarnings("unused")
    public void handleGiveUpButtonClick(ActionEvent actionEvent) {
        serverConnection.sendWithdrawalFlag();
    }

    /**
     * Handles the click's event of Exit button.
     *
     * @param actionEvent The action's event
     */
    @FXML @SuppressWarnings("unused")
    public void handleExitButtonClick(ActionEvent actionEvent) {
        serverConnection.sendExit();
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
            for (PieceFront pf : pieces) pf.getPiece().setDisable(true);
            for (PieceFront pf : rivalPieces) pf.getPiece().setDisable(true);
        });
    }

    @Override
    public void resetGame() {
        Platform.runLater(() -> {
            updateTurn();
            initGame();
            for (Circle p : points)
                p.setVisible(true);
        });
    }

    /**
     * Set the init game config. Set the pieces on the board and enable pieces of the first player.
     */
    private void initGame() {
        pieces = new HashSet<>();
        rivalPieces = new HashSet<>();

        if (serverConnection.isFirstPlayer()) {
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
            for (PieceFront pf : pieces) pf.getPiece().setDisable(true);
            for (PieceFront pf : rivalPieces) pf.getPiece().setDisable(true);
        }
    }

    /**
     * Set the pieces in they rigth positions. The player's pieces are below the board and the opponent's pieces are above.
     */
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

    @Override
    public void drawConfirmation() {
        Platform.runLater(() -> {
            boolean drawResponse = javaFXService.drawConfirmationAlert();

            if (drawResponse)
                serverConnection.sendDrawAcceptedFlag();
            else
                serverConnection.sendDrawDeniedFlag();
        });
    }

    @Override
    public void drawAccepted() {
        Platform.runLater(() ->
                javaFXService.infoAlert("Empate aceito!", "O empate foi aceito. Jogue outra vez!"));
    }

    @Override
    public void drawDenied() {
        Platform.runLater(() ->
                javaFXService.infoAlert("Empate negado!", "O empate foi negado. Tente vencer!"));
    }

    @Override
    public void exit() {
        Platform.runLater(() ->
                javaFXService.goToView("init-view.fxml", (Stage) gameGrid.getScene().getWindow(), new InitController(new ServerConnection())));
    }
}