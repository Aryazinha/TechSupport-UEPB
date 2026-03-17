module br.uepb.techsupport {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;

    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires java.sql;
    requires jbcrypt;

    opens br.uepb.techsupport.model.entidade to org.hibernate.orm.core;
    opens br.uepb.techsupport.model.system to org.hibernate.orm.core;
    opens br.uepb.techsupport.model.enums to org.hibernate.orm.core;

    opens br.uepb.techsupport.controller to javafx.fxml;
    opens br.uepb.techsupport.view to javafx.fxml;

    exports br.uepb.techsupport;
}