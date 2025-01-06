package mertguler.GuiControllers.Admin.Doctor;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mertguler.Exceptions.IDException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Section;
import mertguler.Main;
import mertguler.Person.Doctor;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static mertguler.GuiControllers.Gui.crs;

public class DeleteDoctorGUI implements Initializable {
    private Hospital hospital;
    private Section section;
    private Doctor doctor;

    private InputStream is = Main.class.getResourceAsStream("/images/app_icon.png");
    private Image image = new Image(is);

    @FXML
    private ComboBox<Hospital> hospitalBox;

    @FXML
    private ComboBox<Section> sectionBox;

    @FXML
    private ComboBox<Doctor> doctorBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hospitalBox.setItems(FXCollections.observableArrayList(crs.getHospitals().values()));
    }

    public void setSectionBox(){
        hospital = hospitalBox.getValue();
        sectionBox.setItems(FXCollections.observableArrayList(hospital.getSections()));
    }

    public void setDoctorBox(){
        section = sectionBox.getValue();
        doctorBox.setItems(FXCollections.observableArrayList(section.getDoctors()));
    }


    public void check() {
        doctor = doctorBox.getValue();

        if (hospital == null) {
            showError("Please select a hospital");
            return;
        }
        if (section == null) {
            showError("Please select a section");
            return;
        }
        if (doctor == null) {
            showError("Please select a doctor");
            return;
        }

        try {
            section.deleteDoctor(doctor.getDiploma_id());
            showSuccess("Doctor: " + doctor.getName() + ", Diploma ID: " + doctor.getDiploma_id() + "\nis successfully deleted!");
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

    public void showSuccess(String content) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(null);
        successAlert.setContentText(content);
        Stage stage2 = (Stage) successAlert.getDialogPane().getScene().getWindow();
        stage2.getIcons().add(image);
        successAlert.show();
    }
}
