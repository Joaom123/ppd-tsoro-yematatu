module com.ifce.ppd.tsoroyematatu {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.rmi;

    opens com.ifce.ppd.tsoroyematatu to javafx.fxml;
    exports com.ifce.ppd.tsoroyematatu;
}