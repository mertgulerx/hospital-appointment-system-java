package mertguler;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import mertguler.Enums.City;
import mertguler.Hospital.Hospital;
import mertguler.Hospital.Section;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

import static mertguler.Gui.changeScene;
import static mertguler.Gui.crs;

public class AdminHospitalsGUI implements Initializable {

    @FXML
    public TreeTableView<Object> hospitalsTable;

    @FXML
    public TreeTableColumn<Object, String> nameColumn;
    @FXML
    public TreeTableColumn<Object, String> cityColumn;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        HashMap<Integer, Hospital> hospitals = crs.getHospitals();
        TreeItem<Object> hospitalRoot = new TreeItem<>(new Hospital("Temp Hospital", 0, City.ISTANBUL));

        for (Hospital hospital: hospitals.values()){
            ArrayList<Section> sections = hospital.getSections();
            TreeItem<Object> tempHospital = new TreeItem<>(hospital);
            for (Section section: sections){
                tempHospital.getChildren().add((new TreeItem<>(section)));
            }
            hospitalRoot.getChildren().add(tempHospital);
        }

        nameColumn.setCellValueFactory(param -> {
            TreeItem<Object> cellData = param.getValue();
            if (cellData != null) {
                if (cellData.getValue() instanceof Hospital) {
                    Hospital hospital = (Hospital) cellData.getValue();
                    return new SimpleStringProperty(hospital.getName());
                } else if (cellData.getValue() instanceof Section) {
                    Section section = (Section) cellData.getValue();
                   return new SimpleStringProperty(section.getName());
                }
            }
            return null;
        });

        cityColumn.setCellValueFactory(param -> {
            TreeItem<Object> cellData = param.getValue();
            if (cellData != null) {
                if (cellData.getValue() instanceof Hospital) {
                    Hospital hospital = (Hospital) cellData.getValue();
                    return new SimpleStringProperty(hospital.getCity().toString());
                } else if (cellData.getValue() instanceof Section) {
                    return new SimpleStringProperty("Section");
                }
            }
            return null;
        });

        hospitalsTable.setRoot(hospitalRoot);
        hospitalsTable.setShowRoot(false);
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
    public void showWindow() throws IOException{
       Gui.showWindow("add-hospital-window.fxml");
    }

}
