module application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens application to javafx.fxml;
    opens view.Login to javafx.fxml;
    opens view.Home to javafx.fxml;
    opens view to javafx.fxml;
    opens view.CitizenManagement to javafx.fxml;
    opens model to org.hibernate.orm.core;
    opens controller to javafx.fxml;
    opens service to javafx.fxml;

    exports application;
    exports view.Login;
    exports view.Home;
    exports view;
    exports model;
    exports controller;
    exports service;
}