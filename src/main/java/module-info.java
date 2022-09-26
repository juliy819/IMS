module com.juliy.ims {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.juliy.ims to javafx.fxml;
    exports com.juliy.ims;
}