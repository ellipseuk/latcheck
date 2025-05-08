package com.latcheck;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LatcheckApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Latcheck");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setWidth(800);
        primaryStage.setHeight(640);
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}