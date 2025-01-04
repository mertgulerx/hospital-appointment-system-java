package mertguler;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import mertguler.CRS.DateManager;


import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static mertguler.CRS.CRS.dataPath;
import static mertguler.Gui.crs;


public class SceneController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private Label uiDate;

    @FXML
    private Label label;


    @FXML
    private PieChart hsdChart = new PieChart();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Hospitals", crs.getHospitalManager().getHospitals().size()),
                       new PieChart.Data ("Sections", crs.getHospitalManager().countAllSections()),
        new PieChart.Data("Doctors", crs.getHospitalManager().countAllDoctors()));


        pieChartData.forEach(data ->
                data.nameProperty().bind(
                        Bindings.concat(
                                data.getName(), " : ", data.pieValueProperty()
                        )
                )
        );

        hsdChart.getData().addAll(pieChartData);
    }

    public void switchDataLoadMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Gui.class.getResource("data-load-menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.show();
    }

    public void switchModeSelectMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Gui.class.getResource("mode-select-menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.show();
    }

    public void switchAdminMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Gui.class.getResource("admin-start-menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.show();
    }

    public void loadData(ActionEvent event) throws IOException {
        if (crs.loadTablesFromDisk()){
            label.setText("Successfully loaded data from file: `" + dataPath + "`");
            label.setTextFill(Color.GREEN);
            switchModeSelectMenu(event);
        } else {
            label.setText("Failed to load data from file: `" + dataPath + "`");
            label.setTextFill(Color.RED);
        }
    }

    public void setCurrentDate() {
        String date = DateManager.getCurrentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        uiDate.setText(date);
    }

    public void switchAdminDashboard(ActionEvent event) throws IOException{
        root = FXMLLoader.load(Gui.class.getResource("dashboard-admin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.getScene().setRoot(root);
        stage.show();
    }

    public void initCurrentTime(MouseEvent event){
            if (uiDate.getText().equals("dd/mm/yyyy")){
                setCurrentDate();
            }
    }


    


}
