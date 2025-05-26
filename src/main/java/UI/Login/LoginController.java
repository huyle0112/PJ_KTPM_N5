package UI.Login;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
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
        File brandingFile = new File("Image/Background.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);

        File lockFile = new File("Image/Lock.png");
        Image lockImage = new Image(lockFile.toURI().toString());
        lockImageView.setImage(lockImage);
    }
    public void LoginButtonAction(){
        if(!usernameTextField.getText().isEmpty() || !passwordTextField.getText().isEmpty())
            validateLogin();
        else
            loginMessageLabel.setText("Please enter your username and password.");

    }
    public void cancelButtonAction(ActionEvent actionEvent){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void validateLogin() {
        if(usernameTextField.getText().equals("admin") && passwordTextField.getText().equals("admin")) {
            loginMessageLabel.setText("Login successful!");
        } else {
            loginMessageLabel.setText("Invalid username or password.");
        }
    }
}
