package mertguler.GuiControllers.Doctor.PatientWork;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mertguler.Exceptions.IDException;
import mertguler.Main;
import mertguler.Person.Patient;

import java.io.IOException;
import java.io.InputStream;

import static mertguler.GuiControllers.Gui.crs;
import static mertguler.GuiControllers.Gui.showWindow;

public class SelectPatientGUI {
    private int nationalID;
    public static Patient checkedDoctorsPatient;
    private InputStream is = Main.class.getResourceAsStream("/images/app_icon.png");
    private Image image = new Image(is);

    @FXML
    private TextField nationalIDFIeld;

    public void check() throws IOException {
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
            checkedDoctorsPatient = crs.getPatientManager().getPatient(nationalID);
            System.out.println("Patient with ID: " + nationalID + ", is found");
            if (checkedDoctorsPatient.getRendezvousCount() == 0){
                System.out.println("No rendezvous is found for Patient: " + checkedDoctorsPatient.getName());
                showError("No appointment is found for Patient: " + checkedDoctorsPatient.getName());
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
        showWindow("doctor-rendezvouses-with-patient.fxml", "Your Appointments with Patient: " + checkedDoctorsPatient.getName(), Modality.APPLICATION_MODAL, image);
    }
}
