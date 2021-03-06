module com.ifce.ppd.tsoroyematatu {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.ifce.ppd.tsoroyematatu to javafx.fxml;
    exports com.ifce.ppd.tsoroyematatu;
    exports com.ifce.ppd.tsoroyematatu.controllers;
    opens com.ifce.ppd.tsoroyematatu.controllers to javafx.fxml;
    exports com.ifce.ppd.tsoroyematatu.constants;
    opens com.ifce.ppd.tsoroyematatu.constants to javafx.fxml;
    exports com.ifce.ppd.tsoroyematatu.client;
    opens com.ifce.ppd.tsoroyematatu.client to javafx.fxml;
}