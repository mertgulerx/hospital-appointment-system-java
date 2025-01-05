package mertguler.GuiControllers.Admin;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import javafx.stage.Stage;
import mertguler.CRS.DateManager;
import mertguler.Hospital.Rendezvous;


import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import static mertguler.GuiControllers.Gui.changeScene;
import static mertguler.GuiControllers.Gui.crs;


public class AdminDashboardGUI implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private int[] monthlyRendezvouses = new int[12];
    private AdminStartGUI startGUI = new AdminStartGUI();

    @FXML
    private Label currentYear;

    @FXML
    private Label patientCount;

    @FXML
    private Label doctorCount;

    @FXML
    private Label hospitalCount;

    @FXML
    private Label sectionCount;

    @FXML
    private Label totalRendezvousCount;

    @FXML
    private Label activeRendezvousCount;


    @FXML
    private Label uiDate;

    @FXML
    private LineChart<String, Number> patientVisitsChart;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCurrentDate();
        setPatientCount();
        setDoctorCount();
        setHospitalCount();
        setSectionCount();
        setTotalRendezvousCount();
        setActiveRendezvousCount();
        XYChart.Series series = new XYChart.Series();
        series.setName("Total Appointment Count");
        series.getData().add(new XYChart.Data("Jan", monthlyRendezvouses[0]));
        series.getData().add(new XYChart.Data("Feb", monthlyRendezvouses[1]));
        series.getData().add(new XYChart.Data("March", monthlyRendezvouses[2]));
        series.getData().add(new XYChart.Data("April", monthlyRendezvouses[3]));
        series.getData().add(new XYChart.Data("May", monthlyRendezvouses[4]));
        series.getData().add(new XYChart.Data("June", monthlyRendezvouses[5]));
        series.getData().add(new XYChart.Data("July", monthlyRendezvouses[6]));
        series.getData().add(new XYChart.Data("Aug", monthlyRendezvouses[7]));
        series.getData().add(new XYChart.Data("Sep", monthlyRendezvouses[8]));
        series.getData().add(new XYChart.Data("Oct", monthlyRendezvouses[9]));
        series.getData().add(new XYChart.Data("Nov", monthlyRendezvouses[10]));
        series.getData().add(new XYChart.Data("Dec", monthlyRendezvouses[11]));
        patientVisitsChart.getData().add(series);
        currentYear.setText(String.valueOf(DateManager.getCurrentDate().getYear()));
    }

    public void setCurrentDate() {
        String date = DateManager.getCurrentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        uiDate.setText(date);
    }

    public void updatePatientVisitsChart(){
    }
    public void setPatientCount() {
        patientCount.setText(String.valueOf(crs.getPatientCount()));
    }

    public void setHospitalCount() {
        hospitalCount.setText(String.valueOf(crs.getHospitalCount()));
    }

    public void setSectionCount() {
        sectionCount.setText(String.valueOf(crs.getHospitalManager().countAllSections()));
    }

    public void setDoctorCount() {
        doctorCount.setText(String.valueOf(crs.getHospitalManager().countAllDoctors()));
    }

    public void setTotalRendezvousCount() {
        totalRendezvousCount.setText(String.valueOf(crs.getRendezvousCount()));
    }

    private synchronized void increaseMonthlyRendezvousCount(int index){
        monthlyRendezvouses[index]++;
    }

    public void setActiveRendezvousCount(){
        ArrayList<Rendezvous> rendezvouses = crs.getRendezvouses();
        int[] monthlyRendezvouses = new int[12];
        AtomicInteger count1 = new AtomicInteger();
        AtomicInteger count2 = new AtomicInteger();
        AtomicInteger count3 = new AtomicInteger();
        AtomicInteger count4 = new AtomicInteger();

        int size = rendezvouses.size();
        int quarter = size / 4;
        int half = quarter * 2;
        int threeQuarter = quarter + half;

        Runnable task1 = () ->
        {
            //Thread.currentThread().setName("firstQuarter");
            for (int i = 0; i < quarter; i++) {
                if (!(rendezvouses.get(i).isExpired())) {
                    count1.incrementAndGet();
                }
                if (rendezvouses.get(i).getDate().getYear() == DateManager.getCurrentDate().getYear()){
                    increaseMonthlyRendezvousCount(rendezvouses.get(i).getDate().getMonthValue() - 1);
                }
            }
        };

        Runnable task2 = () ->
        {
            //Thread.currentThread().setName("secondQuarter");
            for (int i = quarter; i < half; i++) {
                if (!(rendezvouses.get(i).isExpired())) {
                    count2.incrementAndGet();
                }
                if (rendezvouses.get(i).getDate().getYear() == DateManager.getCurrentDate().getYear()){
                    increaseMonthlyRendezvousCount(rendezvouses.get(i).getDate().getMonthValue() - 1);
                }
            }
        };

        Runnable task3 = () ->
        {
            //Thread.currentThread().setName("thirdQuarter");
            for (int i = half; i < threeQuarter; i++) {
                if (!(rendezvouses.get(i).isExpired())) {
                    count3.incrementAndGet();
                }
                if (rendezvouses.get(i).getDate().getYear() == DateManager.getCurrentDate().getYear()){
                    increaseMonthlyRendezvousCount(rendezvouses.get(i).getDate().getMonthValue() - 1);
                }
            }
        };

        Runnable task4 = () ->
        {
            //Thread.currentThread().setName("forthQuarter");
            for (int i = threeQuarter; i < size; i++) {
                if (!(rendezvouses.get(i).isExpired())) {
                    count4.incrementAndGet();
                }
                if (rendezvouses.get(i).getDate().getYear() == DateManager.getCurrentDate().getYear()){
                    increaseMonthlyRendezvousCount(rendezvouses.get(i).getDate().getMonthValue() - 1);
                }
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        Thread thread3 = new Thread(task3);
        Thread thread4 = new Thread(task4);
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

        count1.getAndAdd(count2.get());
        count1.getAndAdd(count3.get());
        count1.getAndAdd(count4.get());
        activeRendezvousCount.setText(String.valueOf(count1));
    }

    @FXML
    public void switchAdminDashboard() throws IOException {
        changeScene("admin-dashboard.fxml");
    }

    @FXML
    public void switchAdminHospitals() throws IOException {
        changeScene("admin-hospitals.fxml");
    }




}
