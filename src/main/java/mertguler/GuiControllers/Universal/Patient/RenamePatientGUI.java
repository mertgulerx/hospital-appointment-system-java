package mertguler.GuiControllers.Universal.Patient;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mertguler.Exceptions.IDException;
import mertguler.Main;
import mertguler.Person.Patient;

import java.io.InputStream;
import java.util.Optional;

import static mertguler.GuiControllers.Gui.crs;

public class RenamePatientGUI {
    private int nationalID;
    private InputStream is = Main.class.getResourceAsStream("/images/app_icon.png");
    private Image image = new Image(is);

    @FXML
    private TextField idField;

    public void check() {
        try {
            nationalID = Integer.valueOf(idField.getText());
        } catch (NumberFormatException e) {
            showError("Invalid ID! Please enter a valid number");
            return;
        }

        if (nationalID <= 0){
            showError("National ID, must be greater than 0");
            return;
        }

        try {
            crs.getPatientManager().checkPatientID(nationalID);
            System.out.println("Patient with ID: " + nationalID + ", is found");
            showSuccess();
        } catch (IDException e) {
            System.out.println(e.getMessage());
            showError(e.getMessage());
        }

    }

    public void showError(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(image);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.show();
    }

    public void showSuccess() {
        Patient patient = crs.getPatientManager().getPatient(nationalID);

        Dialog dialog = new TextInputDialog();
        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(image);
        dialog.setTitle("Rename");
        dialog.setHeaderText("Enter new name for Patient: " + patient);
        dialog.setContentText("New name:");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()){
            String newName = result.get();
            if (newName.isBlank()){
                showError("New name cant be empty!");
            } else {
                patient.setName(newName);
                System.out.println("Patient: " + patient + " is successfully renamed to: " + newName);
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Patient: " + patient + " is successfully renamed!\n" +
                        "New name: " + newName);
                Stage stage2 = (Stage) successAlert.getDialogPane().getScene().getWindow();
                stage2.getIcons().add(image);
                successAlert.show();
            }
        }

    }
}
