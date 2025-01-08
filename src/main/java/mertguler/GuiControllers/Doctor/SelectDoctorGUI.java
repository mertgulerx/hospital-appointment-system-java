package mertguler.GuiControllers.Doctor;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Section;
import mertguler.Main;
import mertguler.Person.Doctor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

import static mertguler.GuiControllers.Gui.*;

public class SelectDoctorGUI implements Initializable {
    private Hospital hospital;
    private Section section;
    public static Doctor doctorOfMenu;

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

    public void check() throws IOException {
        doctorOfMenu = doctorBox.getValue();

        if (hospital == null) {
            showError("Please select a hospital");
            return;
        }
        if (section == null) {
            showError("Please select a section");
            return;
        }

        if (doctorOfMenu == null){
            showError("Please select a doctor");
            return;
        }

        switchDoctorMenu();

    }


    public void switchDoctorMenu() throws IOException {
        changeScene("doctor-dashboard.fxml");
    }

    @FXML
    public void goBack() throws IOException{
        changeScene("mode-select-menu.fxml");
    }
}
