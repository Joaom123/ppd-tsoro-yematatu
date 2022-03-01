package com.ifce.ppd.tsoroyematatu.controllers;

import com.ifce.ppd.tsoroyematatu.client.ServerConnection;
import com.ifce.ppd.tsoroyematatu.models.Client;
import com.ifce.ppd.tsoroyematatu.services.JavaFXService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController extends Controller {
    private final ServerConnection serverConnection;
    private final JavaFXService javaFXService = new JavaFXService();

    @FXML
    public TextField playerNameTF;

    @FXML
    public TextField hostNameTF;

    @FXML
    public TextField roomIdTF;

    public LoginController(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        this.serverConnection.setCurrentController(this);
    }

    /**
     * Start the connection with the server. If is connected and playable, go to game-view. If isn't, show message.
     * Validate the login's input.
     *
     * @param actionEvent The action's event
     */
    @SuppressWarnings("unused")
    public void handleLoginButtonClick(ActionEvent actionEvent) {
        String playerName = playerNameTF.getText();
        String hostName = hostNameTF.getText();
        String roomId = roomIdTF.getText();

        if (playerName.isEmpty()) {
            javaFXService.errorAlert("Nome do Jogador vazio!", "O nome do jogador é necessário!");
            return;
        }

        if (hostName.isEmpty()) {
            javaFXService.errorAlert("Hostname vazio!", "Hostname é necessário!");
            return;
        }

        if (roomId.isEmpty()) {
            javaFXService.errorAlert("Identificação da sala vazia!", "Identificação da sala é necessário!");
            return;
        }

        Client client = new Client(playerName);

        serverConnection.startConnection(hostName);
        serverConnection.setClient(client);
        serverConnection.setHostname(hostName);
        serverConnection.setRoomId(roomId);
        try {
            serverConnection.createClientOnServer(roomId);
        } catch (Exception e) {
            e.printStackTrace();
        }


        // If connected: Go to awaiting view
        if (this.serverConnection.isConnected()) {
            Stage actualStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            javaFXService.goToView("awaiting-view.fxml", actualStage, new AwaitingController(serverConnection, actualStage));
            return;
        }

        // If not connected: show error message
        javaFXService.errorAlert("Conexão não estabelecida!",
                "Não foi possível criar uma conexão com o servidor, tente novamente.");
    }

    /**
     * Go to init-view
     *
     * @param actionEvent The action's event
     */
    @SuppressWarnings("unused")
    public void handleBackButtonClick(ActionEvent actionEvent) {
        Stage actualStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        javaFXService.goToView("init-view.fxml", actualStage, new InitController(serverConnection));
    }

    @Override
    public void roomIsFull() {
        Platform.runLater(() -> {
            javaFXService.errorAlert("Sala cheia!", "A sala está cheia, tente outra sala!");
            javaFXService.goToView("init-view.fxml", (Stage) hostNameTF.getScene().getWindow(), new InitController(serverConnection));
        });
    }
}
