package application;

import controller.CitizenController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import service.HibernateUtil;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            HibernateUtil.getSessionFactory();

            // Load màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();
            
            // Tạo scene với màn hình đăng nhập
            Scene scene = new Scene(root);
            
            // Thiết lập stage
            primaryStage.setTitle("Đăng nhập");
            primaryStage.setScene(scene);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} 