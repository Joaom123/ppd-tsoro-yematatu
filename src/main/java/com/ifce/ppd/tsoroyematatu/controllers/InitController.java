package com.ifce.ppd.tsoroyematatu.controllers;

import com.ifce.ppd.tsoroyematatu.TsoroYematatuApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class InitController {

    /**
     * Handle the click event on Init Button ("Iniciar").
     * Start the connection with the server and if the connection is established
     * It change the screen to player enter name. If not, display error message.
     *
     * @param actionEvent The action's event
     */
    public void handleClickInitButton(ActionEvent actionEvent) throws IOException {
        boolean isConnected = false;


        // Try to connect to server

        // Go to next screen
        if (isConnected) {
            Stage actualStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            this.goToGameView(actualStage);
            return;
        }

        // Display error message
        this.errorAlert("Conexão não estabelecida",
                "Não foi possível criar uma conexão com o servidor, tente novamente");
    }

    /**
     * Change scene to Game View Scene
     *
     * @param actualStage
     * @throws IOException
     */
    private void goToGameView(Stage actualStage) throws IOException {
        FXMLLoader gameViewFxmlLoader = new FXMLLoader(TsoroYematatuApplication.class.getResource("game-view.fxml"));
        Scene gameViewScene = new Scene(gameViewFxmlLoader.load(), 967, 791);

        actualStage.setScene(gameViewScene);
        actualStage.show();
    }

    /**
     * Show a alert error message
     *
     * @param headerText
     * @param contentText
     */
    private void errorAlert(String headerText, String contentText) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(headerText);
        errorAlert.setContentText(contentText);
        errorAlert.showAndWait();
    }
}
