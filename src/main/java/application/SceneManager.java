package application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import view.BlueMoonViewController;

import java.io.IOException;

public class SceneManager {

    public SceneManager(){}

    // Mở scene mà không truyền controller
    public void showViewWithOutController(String path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(path));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAndWaitWithOutController(String path, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(path));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Mở scene và truyền controller
    public void showViewWithController(String path, BlueMoonViewController controller, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(path));
            loader.setController(controller);

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
