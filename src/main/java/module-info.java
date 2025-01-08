module mertguler.hospitalreservationsystemjava {
    requires javafx.controls;
    requires javafx.fxml;

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
    exports mertguler.GuiControllers.Admin.Doctor to javafx.fxml;
    opens mertguler.GuiControllers.Admin.Doctor to javafx.fxml;
    exports mertguler.GuiControllers.Universal.Patient to javafx.fxml;
    opens mertguler.GuiControllers.Universal.Patient to javafx.fxml;
    exports mertguler.GuiControllers.Universal.Rendezvous to javafx.fxml;
    opens mertguler.GuiControllers.Universal.Rendezvous to javafx.fxml;
    exports mertguler.GuiControllers.Receptionist to javafx.fxml;
    opens mertguler.GuiControllers.Receptionist to javafx.fxml;
    exports mertguler.GuiControllers.Doctor to javafx.fxml;
    opens mertguler.GuiControllers.Doctor to javafx.fxml;
    exports mertguler.GuiControllers.Doctor.PatientWork to javafx.fxml;
    opens mertguler.GuiControllers.Doctor.PatientWork to javafx.fxml;
}