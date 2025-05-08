package com.latcheck.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public abstract class AbstractController {

    protected void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}