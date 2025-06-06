package view.Login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Button cancelButton;
    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView lockImageView;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File("src/main/resources/image/Background.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("src/main/resources/image/Lock.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }

    public void LoginButtonAction(ActionEvent event) {
        if(!usernameTextField.getText().isEmpty() || !passwordTextField.getText().isEmpty()) {
            if(validateLogin()) {
                try {
                    // Load trang chính
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
                    Parent homeView = loader.load();
                    
                    // Lấy stage hiện tại
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setTitle("Trang chủ");
                    
                    // Tạo scene mới với trang chính
                    Scene scene = new Scene(homeView);
                    
                    
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    loginMessageLabel.setText("Error loading home page");
                }
            }
        } else {
            loginMessageLabel.setText("Please enter your username and password.");
        }
    }

    public void cancelButtonAction(ActionEvent actionEvent){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private boolean validateLogin() {
        if(usernameTextField.getText().equals("admin") && passwordTextField.getText().equals("admin")) {
            loginMessageLabel.setText("Login successful!");
            return true;
        } else {
            loginMessageLabel.setText("Invalid username or password.");
            return false;
        }
    }
}
