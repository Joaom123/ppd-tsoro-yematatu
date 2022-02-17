package com.ifce.ppd.tsoroyematatu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class TsoroYematatuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String fontPath = "fonts/ZilapAfrica.ttf";
        String fontURL = TsoroYematatuApplication.class.getResource(fontPath).toExternalForm();
        Font font = Font.loadFont(fontURL, 30);

        FXMLLoader fxmlLoader = new FXMLLoader(TsoroYematatuApplication.class.getResource("init-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 967, 791);

        Text title = (Text) scene.lookup("#title");
        title.setFont(font);
        stage.setTitle("IFCE - PPD - João Marcus Maia Rocha - Tsoro Yematatu");
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}