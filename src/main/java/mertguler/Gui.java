package mertguler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mertguler.CRS.CRS;

import java.io.IOException;
import java.io.InputStream;


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

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();

        double x = 1280;
        double y = 720;

        if (screenBounds.getMaxX() < 1280 || screenBounds.getMaxY() < 720){
            x = screenBounds.getMaxX() * 0.8;
            y = screenBounds.getMaxY() * 0.8;
        }

        Scene scene =  new Scene(root, screenBounds.getMaxX() * 0.8, screenBounds.getMaxY() * 0.8);

        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}