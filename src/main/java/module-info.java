module com.example.eduassist {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.eduassist to javafx.fxml;
    exports com.eduassist;
}