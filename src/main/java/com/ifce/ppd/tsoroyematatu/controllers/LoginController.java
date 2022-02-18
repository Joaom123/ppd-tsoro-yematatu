package com.ifce.ppd.tsoroyematatu.controllers;

import com.ifce.ppd.tsoroyematatu.client.ServerConnection;
import com.ifce.ppd.tsoroyematatu.models.Client;
import com.ifce.ppd.tsoroyematatu.services.JavaFXService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Controller {
    public TextField playerNameTF;
    public TextField hostNameTF;
    public TextField roomIdTF;
    private final ServerConnection serverConnection;
    private final JavaFXService javaFXService = new JavaFXService();

    public LoginController(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        this.serverConnection.setCurrentController(this);
    }

    /**
     * @param actionEvent
     */
    public void handleLoginButtonClick(ActionEvent actionEvent) {
        String playerName = playerNameTF.getText();
        String hostName = hostNameTF.getText();
        String roomId = roomIdTF.getText();

        if (playerName.isEmpty()) {
            javaFXService.errorAlert("Nome do Jogador vazio!", "O nome do jogador é necessário!");
            return;
        }

        if (hostName.isEmpty()) hostName = "localhost";

        if (roomId.isEmpty()) roomId = "IFCE-PPD";

        Client clientModel = new Client(playerName);

        serverConnection.startConnection(hostName);
        serverConnection.setClientModel(clientModel);
        try {
            serverConnection.createClientOnServer();
        } catch (Exception e) {
            e.printStackTrace();
        }


        // If connected: Go to game screen
        if (this.serverConnection.isConnected()) {
            Stage actualStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            javaFXService.goToView("game-view.fxml", actualStage, new GameController(serverConnection));
            return;
        }

        javaFXService.errorAlert("Conexão não estabelecida!",
                "Não foi possível criar uma conexão com o servidor, tente novamente.");
    }

    /**
     * @param actionEvent
     */
    public void handleBackButtonClick(ActionEvent actionEvent) {
        Stage actualStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        javaFXService.goToView("init-view.fxml", actualStage, new InitController(serverConnection));
    }


    @Override
    public void addMessageToChat(String author, String message) {
    }
}
