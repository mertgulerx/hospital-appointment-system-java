package mertguler;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EnterController {
    @FXML
    private Label welcomeText;

    protected void onEnterButtonClick() {
        welcomeText.setText("Welcome to Hospital Reservation System");
    }
}