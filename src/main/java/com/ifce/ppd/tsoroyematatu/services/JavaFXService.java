package com.ifce.ppd.tsoroyematatu.services;

import com.ifce.ppd.tsoroyematatu.TsoroYematatuApplication;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class JavaFXService {
    /**
     * Load a new scene with the controller of that scene.
     *
     * @param resourceName The path to the resource.
     * @param controller   The controller of the new scene.
     * @param actualStage  The actual stage.
     */
    public void goToView(String resourceName, Stage actualStage, Object controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TsoroYematatuApplication.class.getResource(resourceName));
            fxmlLoader.setController(controller);

            Scene scene = new Scene(fxmlLoader.load(), 967, 791);

            Text title = (Text) scene.lookup("#title");
            if (title != null) title.setFont(this.getZilapAfricaFont());

            actualStage.setScene(scene);
            actualStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            errorAlert("Falha ao carregar nova tela!", "Houve uma falha ao tentar abrir uma nova tela.");
        }

    }

    /**
     * Show error dialog.
     *
     * @param headerText  The header's text
     * @param contentText The content's text
     */
    public void errorAlert(String headerText, String contentText) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(headerText);
        errorAlert.setContentText(contentText);
        errorAlert.show();
    }

    /**
     * Show information dialog.
     *
     * @param headerText  The header's text, if null doesn't show the header
     * @param contentText The content's text
     */
    public void infoAlert(String headerText, String contentText) {
        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setHeaderText(headerText);
        infoAlert.setContentText(contentText);
        infoAlert.show();
    }

    /**
     * Open dialog of draw confirmation with options yes and no.
     *
     * @return True if draw accepted. False if denied.
     */
    public boolean drawConfirmationAlert() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmação do empate");
        alert.setHeaderText("O jogador rival deseja empata! ");
        alert.setContentText("Deseja confirmar o empate? Selecione a opção abaixo");

        ButtonType buttonYes = new ButtonType("Sim", ButtonBar.ButtonData.YES);
        ButtonType buttonNo = new ButtonType("Não", ButtonBar.ButtonData.NO);

        alert.getButtonTypes().setAll(buttonYes, buttonNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == buttonYes;
    }

    /**
     * Get ZilapAfrica font
     *
     * @return The Zilap Africa font
     */
    public Font getZilapAfricaFont() {
        String fontPath = "fonts/ZilapAfrica.ttf";
        String fontURL = Objects.requireNonNull(TsoroYematatuApplication.class.getResource(fontPath)).toExternalForm();
        return Font.loadFont(fontURL, 30);
    }
}
