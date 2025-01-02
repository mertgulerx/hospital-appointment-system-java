package mertguler.GuiControllers;

import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;

public class RatioController {
    public static Scene getScene(Parent root){
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        return new Scene(root, screenBounds.getMaxX() * 0.8, screenBounds.getMaxY() * 0.8);
    }
}
