module mertguler.hospitalreservationsystemjava {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.kordamp.ikonli.materialdesign2;
    requires java.desktop;

    opens mertguler to javafx.fxml;
    exports mertguler;
    exports mertguler.GuiControllers;
    opens mertguler.GuiControllers to javafx.fxml;
    exports mertguler.GuiControllers.Admin;
    opens mertguler.GuiControllers.Admin to javafx.fxml;
    exports mertguler.GuiControllers.MainMenu;
    opens mertguler.GuiControllers.MainMenu to javafx.fxml;
    exports mertguler.GuiControllers.Universal.Lists;
    opens mertguler.GuiControllers.Universal.Lists to javafx.fxml;
    exports mertguler.GuiControllers.Admin.Hospital to javafx.fxml;
    opens mertguler.GuiControllers.Admin.Hospital to javafx.fxml;
    exports mertguler.GuiControllers.Admin.Section to javafx.fxml;
    opens mertguler.GuiControllers.Admin.Section to javafx.fxml;
}