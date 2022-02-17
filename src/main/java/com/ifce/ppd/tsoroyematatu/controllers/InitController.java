package com.ifce.ppd.tsoroyematatu.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class InitController {

    /**
     * Handle the click event on Init Button ("Iniciar").
     * Start the connection with the server and if the connection is established
     * It change the screen to player enter name. If not, display error message.
     * @param actionEvent
     */
    public void handleClickInitButton(ActionEvent actionEvent) {
        boolean isConnected = false;


        // Try to connect to server

        // Go to next screen
        if (isConnected){

            return;
        }

        // Display error message
        this.errorAlert("Conexão não estabelecida",
                "Não foi possível criar uma conexão com o servidor, tente novamente");
    }

    /**
     * Show a alert error message
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
