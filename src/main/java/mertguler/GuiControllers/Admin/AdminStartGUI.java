package mertguler.GuiControllers.Admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import mertguler.CRS.DateManager;
import mertguler.Main;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static mertguler.GuiControllers.Gui.changeScene;
import static mertguler.GuiControllers.Gui.showWindow;

public class AdminStartGUI implements Initializable {

    @FXML
    private Label uiDate;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCurrentDate();
    }

    public void setCurrentDate() {
        String date = DateManager.getCurrentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        uiDate.setText(date);
    }

    @FXML
    public void switchAdminDashboard() throws IOException {
        changeScene("admin-dashboard.fxml");
    }

    @FXML
    public void switchAdminHospitals() throws IOException {
        changeScene("admin-hospitals.fxml");
    }

    @FXML
    public void showHospitalListWindow() throws IOException {
        InputStream is = Main.class.getResourceAsStream("/images/hospital.png");
        Image image = new Image(is);
        showWindow("hospital-list.fxml", "Hospital List", Modality.NONE, image);
    }

    @FXML
    public void showPatientListWindow() throws IOException {
        InputStream is = Main.class.getResourceAsStream("/images/patients.png");
        Image image = new Image(is);
        showWindow("patient-list.fxml", "Patient List", Modality.NONE, image);
    }

    @FXML
    public void showDoctorListWindow() throws IOException {
        InputStream is = Main.class.getResourceAsStream("/images/doctor-menu.png");
        Image image = new Image(is);
        showWindow("doctor-list.fxml", "Doctor List", Modality.NONE, image);
    }

    @FXML
    public void showSectionListWindow() throws IOException {
        InputStream is = Main.class.getResourceAsStream("/images/sections.png");
        Image image = new Image(is);
        showWindow("section-list.fxml", "Section List", Modality.NONE, image);
    }

    @FXML
    public void showRendezvousListWindow() throws IOException {
        InputStream is = Main.class.getResourceAsStream("/images/appointment.png");
        Image image = new Image(is);
        showWindow("rendezvous-list.fxml", "Appointment List", Modality.NONE, image);
    }

    @FXML
    public void showAddHospitalWindow() throws IOException {
        InputStream is = Main.class.getResourceAsStream("/images/add.png");
        Image image = new Image(is);
        showWindow("add-hospital.fxml", "Add Hospital", Modality.APPLICATION_MODAL, image);
    }

    @FXML
    public void showDeleteHospitalWindow() throws IOException {
        InputStream is = Main.class.getResourceAsStream("/images/delete.png");
        Image image = new Image(is);
        showWindow("delete-hospital.fxml", "Delete Hospital", Modality.APPLICATION_MODAL, image);
    }

    @FXML
    public void showRenameHospitalWindow() throws IOException {
        InputStream is = Main.class.getResourceAsStream("/images/rename.png");
        Image image = new Image(is);
        showWindow("rename-hospital.fxml", "Rename Hospital", Modality.APPLICATION_MODAL, image);
    }

}
