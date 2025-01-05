package mertguler.GuiControllers.MainMenu;

import javafx.event.ActionEvent;

import java.io.IOException;

import static mertguler.GuiControllers.Gui.changeScene;

public class ModeSelectGUI {

    public void switchAdminMenu(ActionEvent event) throws IOException {
        changeScene("admin-start-menu.fxml");
    }


}
