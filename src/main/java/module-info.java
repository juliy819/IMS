module com.juliy.ims {
    requires javafx.controls;
    requires javafx.fxml;

    requires rxcontrols;
    requires lombok;
    requires java.sql;

    opens com.juliy.ims.controller to javafx.fxml;
    exports com.juliy.ims;
}