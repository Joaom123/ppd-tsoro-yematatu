package com.ifce.ppd.tsoroyematatu.controllers;

import com.ifce.ppd.tsoroyematatu.client.ServerConnection;
import com.ifce.ppd.tsoroyematatu.services.JavaFXService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AwaitingController extends Controller implements Initializable {
    private final ServerConnection serverConnection;
    private final JavaFXService javaFXService = new JavaFXService();
    @FXML
    public Text playerName;
    @FXML
    public Text host;
    @FXML
    public Text roomId;
    private final Stage stage;

    public AwaitingController(ServerConnection serverConnection, Stage actualStage) {
        this.serverConnection = serverConnection;
        this.serverConnection.setCurrentController(this);
        this.stage = actualStage;
    }


    @Override
    public void goToGame() {
        System.out.println("Go to game");
        Platform.runLater(() -> javaFXService.goToView("game-view.fxml", stage, new GameController(serverConnection)));
    }

    @FXML @SuppressWarnings("unused")
    public void handleExitButtonClick(ActionEvent actionEvent) {
        serverConnection.sendExit();

        Stage actualStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        javaFXService.goToView("login-view.fxml", actualStage, new LoginController(serverConnection));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playerName.setText(serverConnection.getClient().getName());
        host.setText(serverConnection.getHostname());
        roomId.setText(serverConnection.getRoomId());
    }
}
