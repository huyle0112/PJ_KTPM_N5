package View;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Household;
import model.Citizen;
import service.HouseholdService;
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

    private HouseholdService householdService;
    private ObservableList<Household> householdList;

    @FXML
    public void initialize() {
        householdService = new HouseholdService();
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
            String headName = head != null ? head.getFullName() : "";
            return javafx.beans.binding.Bindings.createStringBinding(() -> headName);
        });
    }

    private void loadHouseholds() {
        List<Household> households = householdService.getAllHouseholds();
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
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                householdTable.setItems(householdList);
            } else {
                ObservableList<Household> filteredList = householdList.filtered(household ->
                    household.getAddress().toLowerCase().contains(newValue.toLowerCase()) ||
                    (household.getHead() != null && 
                     household.getHead().getFullName().toLowerCase().contains(newValue.toLowerCase()))
                );
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
                householdService.deleteHousehold(household.getId());
                loadHouseholds();
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
} 