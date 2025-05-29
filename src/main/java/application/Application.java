package application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML từ resources
            Parent root = FXMLLoader.load(getClass().getResource("/ResidentChargeView.fxml"));

            Scene scene = new Scene(root);
            primaryStage.setTitle("Quản lý khoản thu - Blue Moon");
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
