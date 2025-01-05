package mertguler.GuiControllers.Admin;


import javafx.fxml.FXML;


import java.io.IOException;
import static mertguler.GuiControllers.Gui.changeScene;

public class AdminHospitalsGUI {


    @FXML
    public void switchAdminDashboard() throws IOException {
        changeScene("admin-dashboard.fxml");
    }

    @FXML
    public void switchAdminHospitals() throws IOException {
        changeScene("admin-hospitals.fxml");
    }


}
