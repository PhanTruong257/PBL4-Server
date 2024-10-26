module pbl4.server {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens pbl4.server to javafx.fxml;
    exports pbl4.server;
}