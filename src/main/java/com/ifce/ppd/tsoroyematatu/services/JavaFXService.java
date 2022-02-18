package com.ifce.ppd.tsoroyematatu.services;

import com.ifce.ppd.tsoroyematatu.TsoroYematatuApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class JavaFXService {
    /**
     * Change scene to a new View Scene.
     * Set the controller.
     *
     * @param actualStage
     * @throws IOException
     */
    public void goToView(String resourceName, Stage actualStage, Object controller) {
        FXMLLoader fxmlLoader = new FXMLLoader(TsoroYematatuApplication.class.getResource(resourceName));
        fxmlLoader.setController(controller);

        try {
            actualStage.setScene(new Scene(fxmlLoader.load(), 967, 791));
            actualStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            errorAlert("Falha ao carregar nova tela!", "Houve uma falha ao tentar abrir uma nova tela.");
        }

    }

    /**
     * Show a alert error message
     *
     * @param headerText  The header's text
     * @param contentText The content's text
     */
    public void errorAlert(String headerText, String contentText) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(headerText);
        errorAlert.setContentText(contentText);
        errorAlert.showAndWait();
    }

    /**
     * Get ZilapAfrica font
     *
     * @return The Zilap Africa font
     */
    public Font getZilapAfricaFont() {
        String fontPath = "fonts/ZilapAfrica.ttf";
        String fontURL = TsoroYematatuApplication.class.getResource(fontPath).toExternalForm();
        return Font.loadFont(fontURL, 30);
    }
}
