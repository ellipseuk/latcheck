package com.latcheck;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LatcheckApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Latcheck");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setWidth(720);
        primaryStage.setHeight(540);
        primaryStage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}