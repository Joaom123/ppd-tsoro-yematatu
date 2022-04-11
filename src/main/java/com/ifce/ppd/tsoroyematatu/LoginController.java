package com.ifce.ppd.tsoroyematatu;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class LoginController extends Controller {
    private final ServerConnection serverConnection;
    private final JavaFXService javaFXService = new JavaFXService();
    private Stage stage;

    @FXML
    public TextField playerNameTF;

    @FXML
    public TextField hostNameTF;

    @FXML
    public TextField roomIdTF;

    public LoginController(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
        this.serverConnection.setCurrentController(this);
        serverConnection.startConnection();
    }

    /**
     * Start the connection with the server. If is connected and playable, go to game-view. If isn't, show message.
     * Validate the login's input.
     *
     * @param actionEvent The action's event
     */
    @SuppressWarnings("unused")
    public void handleLoginButtonClick(ActionEvent actionEvent) throws NullClientException, RemoteException {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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
        serverConnection.setClient(client);
        serverConnection.setHostname(hostName);
        serverConnection.setRoomId(roomId);
        serverConnection.createClientOnServer(roomId);
//        try {
//
//        } catch (Exception e) {
//            System.out.println("Erro ao criar cliente no servidor");
//            e.printStackTrace();
//            // If not connected: show error message
//            javaFXService.errorAlert("Conexão não estabelecida!",
//                    "Não foi possível criar uma conexão com o servidor, tente novamente.");
//            return;
//        }


        // If connected: Go to awaiting view
        if (serverConnection.isConnected())
            javaFXService.goToView("awaiting-view.fxml", stage, new AwaitingController(serverConnection, stage));
    }

    @Override
    public void goToGame() {
        Platform.runLater(() -> javaFXService.goToView("game-view.fxml", stage, new GameController(serverConnection)));
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
