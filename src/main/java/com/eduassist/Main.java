package com.eduassist;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static final Image icon = new Image("file:" + Main.class.getResource("images/Icon.png").getPath().replace("%20", " "));
    private static Stage stg;

    @Override
    public void start(Stage stage) throws IOException {
        stg = stage;
        stage.setResizable(false);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LogIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Iniciar Sesión");
        stage.getIcons().add(icon);
        stage.setScene(scene);

        stage.show();
    }

    public void changeScene(String fxml, String title) throws IOException {

        Parent pane = FXMLLoader.load(getClass().getResource(fxml));
        stg.setWidth(1200);
        stg.setHeight(600);
        stg.setResizable(true);
        stg.setTitle(title);
        stg.getScene().setRoot(pane);
        stg.setMaximized(true);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Image getIcon() {
        return icon;
    }
}