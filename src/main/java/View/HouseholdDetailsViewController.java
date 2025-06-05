package view;

import controller.CitizenController;
import controller.HouseholdController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Household;
import model.Citizen;
import java.util.List;
import javafx.collections.FXCollections;

public class HouseholdDetailsViewController {
    @FXML
    private TextField addressField;
    @FXML
    private ComboBox<Citizen> headComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Household household;
    private HouseholdController householdController;
    private CitizenController citizenController;
    private Runnable onSaveCallback;

    @FXML
    public void initialize() {
        householdController = new HouseholdController();
        citizenController = new CitizenController();
        setupButtons();
        loadCitizens();
    }

    private void setupButtons() {
        saveButton.setOnAction(event -> saveHousehold());
        cancelButton.setOnAction(event -> closeDialog());

    }

    private void loadCitizens() {
        List<Citizen> citizens = citizenController.getAllCitizens();
        headComboBox.setItems(FXCollections.observableArrayList(citizens));
        headComboBox.setCellFactory(param -> new ListCell<Citizen>() {
            @Override
            protected void updateItem(Citizen citizen, boolean empty) {
                super.updateItem(citizen, empty);
                if (empty || citizen == null) {
                    setText(null);
                } else {
                    setText(citizen.getFullname());
                }
            }
        });
        headComboBox.setButtonCell(headComboBox.getCellFactory().call(null));
    }

    public void setHousehold(Household household) {
        this.household = household;
        if (household != null) {
            addressField.setText(household.getAddress());
            headComboBox.setValue(household.getHead());
        }
    }

    public void setOnSaveCallback(Runnable callback) {
        this.onSaveCallback = callback;
    }

    private void saveHousehold() {
        if (!validateInput()) {
            return;
        }

        if (household == null) {
            household = new Household();
        }

        household.setAddress(addressField.getText());
        household.setHead(headComboBox.getValue());

        try {
            householdController.addHousehold(household);
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            closeDialog();
        } catch (Exception e) {
            showAlert("Error saving household: " + e.getMessage());
        }
    }

    private boolean validateInput() {
        if (addressField.getText() == null || addressField.getText().trim().isEmpty()) {
            showAlert("Please enter an address");
            return false;
        }
        if (headComboBox.getValue() == null) {
            showAlert("Please select a household head");
            return false;
        }
        return true;
    }

    private void closeDialog() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 