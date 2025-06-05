package view;

import controller.HouseholdController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Household;
import model.Citizen;
import java.io.IOException;
import java.util.List;

public class HouseholdViewController {
    @FXML
    private TableView<Household> householdTable;
    @FXML
    private TableColumn<Household, Integer> idColumn;
    @FXML
    private TableColumn<Household, String> addressColumn;
    @FXML
    private TableColumn<Household, String> headNameColumn;
    @FXML
    private Button addButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TextField searchField;

    private HouseholdController householdController;
    private ObservableList<Household> householdList;

    @FXML
    public void initialize() {
        householdController = new HouseholdController();
        setupTable();
        loadHouseholds();
        setupButtons();
        setupSearch();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        headNameColumn.setCellValueFactory(cellData -> {
            Citizen head = cellData.getValue().getHead();
            String headName = head != null ? head.getFullname() : "";
            return javafx.beans.binding.Bindings.createStringBinding(() -> headName);
        });
    }

    private void loadHouseholds() {
        List<Household> households = householdController.getAllHouseholds();
        households.sort((h1, h2) -> Integer.compare(h1.getId(), h2.getId()));
        householdList = FXCollections.observableArrayList(households);
        householdTable.setItems(householdList);
    }

    private void setupButtons() {
        addButton.setOnAction(event -> openHouseholdDialog(null));
        editButton.setOnAction(event -> {
            Household selected = householdTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                openHouseholdDialog(selected);
            } else {
                showAlert("Please select a household to edit");
            }
        });
        deleteButton.setOnAction(event -> {
            Household selected = householdTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                deleteHousehold(selected);
            } else {
                showAlert("Please select a household to delete");
            }
        });
        householdTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // click đôi, đổi thành 1 nếu muốn click đơn
                Household selected = householdTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/HouseholdCitizenView.fxml"));
                        Parent root = loader.load();
                        view.HouseholdCitizenViewController controller = loader.getController();
                        controller.showCitizensOfHousehold(selected.getId());
                        Stage stage = new Stage();
                        stage.setTitle("Citizens of Household");
                        stage.setScene(new Scene(root));
                        stage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                        showAlert("Error opening citizens by household view");
                    }
                }
            }
        });
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                householdTable.setItems(householdList);
            } else {
                List<Household> households = householdController.findHouseholdsByOwnerName(newValue);
                ObservableList<Household> filteredList = FXCollections.observableArrayList(households);
                householdTable.setItems(filteredList);
            }
        });
    }

    private void openHouseholdDialog(Household household) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HouseholdDetailsView.fxml"));
            Parent root = loader.load();
            HouseholdDetailsViewController controller = loader.getController();
            controller.setHousehold(household);
            controller.setOnSaveCallback(this::loadHouseholds);

            Stage stage = new Stage();
            stage.setTitle(household == null ? "Add New Household" : "Edit Household");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error opening household dialog");
        }
    }

    private void deleteHousehold(Household household) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Household");
        alert.setHeaderText("Delete Household");
        alert.setContentText("Are you sure you want to delete this household?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                householdController.deleteHousehold(household.getId());
                loadHouseholds();
            }
        });
    }

    @FXML
    private void returnHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent citizenView = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(citizenView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 