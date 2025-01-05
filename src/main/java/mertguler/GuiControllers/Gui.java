package mertguler.GuiControllers;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import mertguler.CRS.CRS;
import mertguler.CRS.DateManager;
import mertguler.Main;

import java.io.IOException;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class Gui extends Application {
    public static CRS crs = new CRS();
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        // Can it improve text quality??
        System.setProperty("prism.lcdtext", "false");
        Application.setUserAgentStylesheet(Main.class.getResource("mac-light.css").toExternalForm());
        //Application.setUserAgentStylesheet(Gui.class.getResource("win-light.css").toExternalForm());
        Parent root = FXMLLoader.load(Main.class.getResource("login-menu.fxml"));

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

    public static void changeScene(String fxml) throws IOException {
        Parent pane = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(fxml)));
        primaryStage.getScene().setRoot(pane);
    }

    public static void showWindow(String fxml, String title, Modality modality, Image image) throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(fxml)));
        Stage stage = new Stage();
        stage.setTitle(title);
        Scene scene =  new Scene(root);
        if (image != null){
            stage.getIcons().add(image);
        }
        stage.setScene(scene);
        stage.initModality(modality);
        stage.show();
    }

    public static String getUiDate(){
        return DateManager.getCurrentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public static void main(String[] args) {
        launch();
    }
}