module com.juliy.ims {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires rxcontrols;
    requires lombok;

    opens com.juliy.ims.controller to javafx.fxml;
    exports com.juliy.ims;
}