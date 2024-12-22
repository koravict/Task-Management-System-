module com.example.testing {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.fasterxml.jackson.core;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.google.gson;

    opens com.example.testing to javafx.fxml, com.google.gson;
    exports com.example.testing;
}