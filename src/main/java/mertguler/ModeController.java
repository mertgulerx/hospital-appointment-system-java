package mertguler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mertguler.CRS.CRS;

import java.io.IOException;

import static mertguler.GuiControllers.RatioController.getScene;

public class ModeController {
    private Stage stage;
    private Scene scene;
    private Parent root;


    public void switchModeSelectMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Gui.class.getResource("mode-select-menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = getScene(root);
        stage.setScene(scene);
        stage.show();
    }
}
