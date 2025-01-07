package mertguler.GuiControllers.Admin.Section;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mertguler.Enums.City;
import mertguler.Exceptions.DuplicateInfoException;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Section;
import mertguler.Main;
import org.controlsfx.control.ToggleSwitch;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static mertguler.GuiControllers.Gui.crs;


public class AddSectionGUI implements Initializable {
    private String name;
    private Hospital hospital;
    private boolean isChildOnly = false;
    private InputStream is = Main.class.getResourceAsStream("/images/app_icon.png");
    private Image image = new Image(is);

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<Hospital> hospitalBox;

    @FXML
    private ToggleButton childSwitch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        hospitalBox.setItems(FXCollections.observableArrayList(crs.getHospitals().values()));
    }


    public void changeChild(){
        if (isChildOnly){
            isChildOnly = false;
        } else {
            isChildOnly = true;
        }
    }

    public void check() {
        name = nameField.getText();
        hospital = hospitalBox.getValue();

        if (name.isBlank()) {
            showError("Name cant be empty");
            return;
        }

        if (hospital == null) {
            showError("Please select a hospital");
        }

        try {
            Section section = new Section(name, hospital, isChildOnly);
            hospital.addSection(section);
            System.out.println("Section is added to Hospital: " + hospital + ": with name: " + name + ", ID: " + section.getId()
                    + ", Child Only: " + isChildOnly);
            showSuccess("Section is added to Hospital: " + hospital,"Section Name: " + name + "\nSection ID: "+ section.getId() +  "\nChild Only: " + isChildOnly);
        } catch (DuplicateInfoException e) {
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

    public void showSuccess(String header, String content) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Success");
        successAlert.setHeaderText(header);
        successAlert.setContentText(content);
        Stage stage2 = (Stage) successAlert.getDialogPane().getScene().getWindow();
        stage2.getIcons().add(image);
        successAlert.show();
    }

}
