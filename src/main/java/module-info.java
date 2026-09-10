module com.fabrica.stock.desktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.net.http;

    opens com.fabrica.stock.desktop to javafx.fxml;

    opens com.fabrica.stock.desktop.principal.ui to javafx.fxml;
    opens com.fabrica.stock.desktop.terminal.ui to javafx.fxml;
    opens com.fabrica.stock.desktop.mezcla.ui to javafx.fxml;

    opens com.fabrica.stock.desktop.terminal.api to com.fasterxml.jackson.databind;
    opens com.fabrica.stock.desktop.mezcla.api to com.fasterxml.jackson.databind;

    exports com.fabrica.stock.desktop;
}