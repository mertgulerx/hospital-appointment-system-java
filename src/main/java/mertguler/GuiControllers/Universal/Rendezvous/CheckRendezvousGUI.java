package mertguler.GuiControllers.Universal.Rendezvous;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mertguler.Exceptions.IDException;
import mertguler.GuiControllers.Gui;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Rendezvous;
import mertguler.Main;
import mertguler.Person.Patient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static mertguler.GuiControllers.Gui.crs;
import static mertguler.GuiControllers.Gui.showWindow;

public class CheckRendezvousGUI {
    private int nationalID;
    public static Patient patientForPatientRendezvousList;
    private InputStream is = Main.class.getResourceAsStream("/images/app_icon.png");
    private Image image = new Image(is);

    @FXML
    private TextField nationalIDFIeld;

    public void check() throws IOException{
        try {
            nationalID = Integer.valueOf(nationalIDFIeld.getText());
        } catch (NumberFormatException e) {
            showError("Invalid ID! Please enter a valid number");
            return;
        }

        if (nationalID <= 0){
            showError("National ID, must be greater than 0");
            return;
        }

        try {
            patientForPatientRendezvousList = crs.getPatientManager().getPatient(nationalID);
            System.out.println("Patient with ID: " + nationalID + ", is found");
            if (patientForPatientRendezvousList.getRendezvousCount() == 0){
                System.out.println("No rendezvous found for Patient: " + patientForPatientRendezvousList.getName());
                showError("No appointment found for Patient: " + patientForPatientRendezvousList.getName());
            } else {
                showSuccess();

            }

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

    public void showSuccess() throws IOException{
            InputStream is = Main.class.getResourceAsStream("/images/appointment.png");
            Image image = new Image(is);
            showWindow("patient-rendezvous-list.fxml", "Rendezvous List for Patient: " + patientForPatientRendezvousList.getName(), Modality.APPLICATION_MODAL, image);
    }


}
