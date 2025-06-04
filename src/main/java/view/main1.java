package view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.Login.LoginMain;

public class main1  extends Application {
        @Override
        public void start(Stage primaryStage) throws Exception {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginMain.class.getResource("/ResidentChargeView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {launch(args);}
}
