package com.ifce.ppd.tsoroyematatu.controllers;

import com.ifce.ppd.tsoroyematatu.client.ServerConnection;
import com.ifce.ppd.tsoroyematatu.services.JavaFXService;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;


/**
 * The controller of init-view
 */
public class InitController implements Controller {
    ServerConnection serverConnection;
    JavaFXService javaFXService = new JavaFXService();

    public InitController(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }

    /**
     * Handle the click event on Init Button ("Iniciar").
     * After the click, the user is directed to the Login View.
     * @param actionEvent The action's event
     */
    public void handleClickInitButton(ActionEvent actionEvent) {
        Stage actualStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        javaFXService.goToView("login-view.fxml", actualStage, new LoginController(serverConnection));
    }


    @Override
    public void addMessageToChat(String author, String message) {}
}

