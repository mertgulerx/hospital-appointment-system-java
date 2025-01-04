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
    requires java.datatransfer;

    opens mertguler to javafx.fxml;
    exports mertguler;
}