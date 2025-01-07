package mertguler.GuiControllers.Universal.Rendezvous;


import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mertguler.CRS.DateManager;
import mertguler.Exceptions.DailyLimitException;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Exceptions.IDException;
import mertguler.Exceptions.RendezvousLimitException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Rendezvous;
import mertguler.Hospital.Section;
import mertguler.Main;
import mertguler.Person.Doctor;
import mertguler.Person.Patient;

import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import static mertguler.GuiControllers.Gui.crs;

public class DeleteRendezvousGUI {
    private long nationalID;
    private Patient patient;
    private Rendezvous rendezvous;
    private InputStream is = Main.class.getResourceAsStream("/images/app_icon.png");
    private Image image = new Image(is);

    @FXML
    private TextField nationalIDField;

    @FXML
    private ComboBox<Rendezvous> rendezvousBox;

    @FXML
    private Button button;


    public void setRendezvousBox(){
        try {
            nationalID = Integer.valueOf(nationalIDField.getText());
        } catch (NumberFormatException e) {
            showError("Please enter a valid National ID");
            return;
        }

        if (nationalID <= 0) {
            showError("National ID, must be greater than 0");
            return;
        }

        try {
            patient = crs.getPatientManager().getPatient(nationalID);
        } catch (IDException e) {
            System.out.println(e.getMessage());
            showError(e.getMessage());
            return;
        }

        ArrayList<Rendezvous> rendezvouses = patient.getRendezvouses();

        if (rendezvouses.isEmpty()){
            System.out.println("No rendezvous found for Patient: " + patient);
            showError("No rendezvous found for:\nPatient: " + patient);
            return;
        }

        rendezvousBox.setItems(FXCollections.observableArrayList(patient.getRendezvouses()));
        button.setText("Cancel");
    }


    public void check() {
        if (button.getText().equals("Check")){
            setRendezvousBox();
        } else {
            showSuccess();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(image);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure? Please confirm the cancellation. This action cannot be undone.");
        ButtonType confirmButton = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Keep Appointment", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == confirmButton) {
            crs.deleteRendezvous(rendezvous);
            System.out.println("Appointment: " + rendezvous + " is cancelled");
        }
    }
}
