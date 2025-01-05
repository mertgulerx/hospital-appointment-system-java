package mertguler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mertguler.CRS.DateManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static mertguler.Gui.changeScene;
import static mertguler.Gui.showWindow;

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

}
