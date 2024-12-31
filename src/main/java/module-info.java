module mertguler.hospitalreservationsystemjava {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens mertguler to javafx.fxml;
    exports mertguler;
    exports mertguler.Person;
    opens mertguler.Person to javafx.fxml;
    exports mertguler.TextUI;
    opens mertguler.TextUI to javafx.fxml;
    exports mertguler.TextUI.Menu;
    opens mertguler.TextUI.Menu to javafx.fxml;
    exports mertguler.CRS;
    opens mertguler.CRS to javafx.fxml;
}