package view.Home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.io.IOException;

public class HomeController {
    @FXML
    private Button citizenButton;
    @FXML
    private Button householdButton;
    @FXML
    private Button residentChargeButton;
    @FXML
    private Button logoutButton;
    @FXML
    private StackPane rootPane;

    private void loadViewWithIndicator(String fxmlPath, String title, Button buttonToDisable) {
        // Create and show loading indicator
        ProgressIndicator loadingIndicator = new ProgressIndicator();
        loadingIndicator.setMaxSize(100, 100);
        rootPane.getChildren().add(loadingIndicator);
        loadingIndicator.setVisible(true);
        
        buttonToDisable.requestFocus();
        buttonToDisable.setStyle("-fx-opacity: 0.5;"); // Add visual feedback that button is disabled

        // Load the view in a background thread
        new Thread(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
                Parent view = loader.load();
                
                // Update UI on JavaFX Application Thread
                Platform.runLater(() -> {
                    try {
                        Stage stage = (Stage) rootPane.getScene().getWindow();
                        if (title != null) {
                            stage.setTitle(title);
                        }
                        Scene scene = new Scene(view);
                        stage.setScene(scene);
                        stage.show();
                    } finally {
                        // Remove loading indicator and re-enable button
                        rootPane.getChildren().remove(loadingIndicator);
                        buttonToDisable.setStyle(""); // Reset button style
                    }
                });
            } catch (IOException e) {
                Platform.runLater(() -> {
                    rootPane.getChildren().remove(loadingIndicator);
                    buttonToDisable.setStyle(""); // Reset button style
                    e.printStackTrace();
                });
            }
        }).start();
    }

    @FXML
    public void openCitizenView(ActionEvent event) {
        loadViewWithIndicator("/Citizen.fxml", "Quản lý nhân khẩu", citizenButton);
    }

    @FXML
    public void openHouseholdView(ActionEvent event) {
        loadViewWithIndicator("/HouseholdView.fxml", "Quản lý Hộ khẩu", householdButton);
    }

    @FXML
    public void openResidentChargeView(ActionEvent event) {
        loadViewWithIndicator("/ResidentChargeView.fxml", "Quản lý khoản thu", residentChargeButton);
    }

    @FXML
    public void logout(ActionEvent event) {
        loadViewWithIndicator("/Login.fxml", "Đăng nhập", logoutButton);
    }
} 