module application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens view.CitizenManagement to javafx.fxml;
    opens application to javafx.fxml;
    opens view to javafx.fxml;
    opens view.Login to javafx.fxml;
    opens model to org.hibernate.orm.core;
    exports model;
    exports view;
    exports view.CitizenManagement;
    exports view.Login;
    exports application;
}