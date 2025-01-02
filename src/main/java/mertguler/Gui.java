package mertguler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mertguler.CRS.CRS;

import java.io.IOException;
import java.io.InputStream;

import static mertguler.GuiControllers.RatioController.getScene;

public class Gui extends Application {
    public static CRS crs = new CRS();

    @Override
    public void start(Stage stage) throws IOException {
        // Can it improve text quality??
        System.setProperty("prism.lcdtext", "false");
        Application.setUserAgentStylesheet(Gui.class.getResource("mac-light.css").toExternalForm());
        //Application.setUserAgentStylesheet(Gui.class.getResource("win-light.css").toExternalForm());
        Parent root = FXMLLoader.load(Gui.class.getResource("login-menu.fxml"));

        stage.setTitle("Hospital Rendezvous System");

        InputStream is = Main.class.getResourceAsStream("/images/app_icon.png");
        Image image = new Image(is);
        stage.getIcons().add(image);

        Scene scene = getScene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}