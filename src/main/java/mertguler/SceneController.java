package mertguler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import mertguler.CRS.CRS;
import org.w3c.dom.events.MouseEvent;


import java.io.IOException;
import java.io.InputStream;

import static mertguler.CRS.CRS.dataPath;
import static mertguler.Gui.crs;
import static mertguler.GuiControllers.RatioController.getScene;


public class SceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label label;

    public void switchDataLoadMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Gui.class.getResource("data-load-menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = getScene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void loadData(ActionEvent event) {
        if (crs.loadTablesFromDisk()){
            label.setText("Successfully loaded data from file: `" + dataPath + "`");
            label.setTextFill(Color.GREEN);
        } else {
            label.setText("Failed to load data from file: `" + dataPath + "`");
            label.setTextFill(Color.RED);
        }
    }


}
