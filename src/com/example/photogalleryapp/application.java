package com.example.photogalleryapp;

import com.example.photogalleryapp.controllers.loginPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class application extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        URL fxmlLocation = getClass().getResource("/com/example/photogalleryapp/views/loginPageView.fxml");
        loader.setLocation(fxmlLocation);

        GridPane root = loader.load();

        loginPageController loginPageController = loader.getController();
        loginPageController.start(primaryStage);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
