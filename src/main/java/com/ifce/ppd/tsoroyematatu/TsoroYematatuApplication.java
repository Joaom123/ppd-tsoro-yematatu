package com.ifce.ppd.tsoroyematatu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class TsoroYematatuApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(TsoroYematatuApplication.class.getResource("init-view.fxml"));
        fxmlLoader.setController(new InitController(new ServerConnection()));

        Scene scene = new Scene(fxmlLoader.load(), 967, 791);

        Text title = (Text) scene.lookup("#title");
        title.setFont(this.getZilapAfricaFont());

        stage.setTitle("IFCE - PPD - João Marcus Maia Rocha - Tsoro Yematatu");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Get ZilapAfrica font
     *
     * @return The Zilap Africa font
     */
    private Font getZilapAfricaFont() {
        String fontPath = "fonts/ZilapAfrica.ttf";
        String fontURL = TsoroYematatuApplication.class.getResource(fontPath).toExternalForm();
        return Font.loadFont(fontURL, 30);
    }
}