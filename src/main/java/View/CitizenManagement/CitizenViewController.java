package view.CitizenManagement;

import controller.CitizenController;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Citizen;
import model.Household;
import model.Room;

import java.io.IOException;
import java.time.LocalDate;

public class CitizenViewController {

    @FXML private TableView<Citizen> citizenTable;
    @FXML private TableColumn<Citizen, Integer> idColumn;
    @FXML private TableColumn<Citizen, String> fullnameColumn;
    @FXML private TableColumn<Citizen, LocalDate> dobColumn;
    @FXML private TableColumn<Citizen, String> placeOfBirthColumn;
    @FXML private TableColumn<Citizen, String> occupationColumn;
    @FXML private TableColumn<Citizen, String> nationalIdColumn;
    @FXML private TableColumn<Citizen, Citizen.ResidencyStatus> statusColumn;
    @FXML private TableColumn<Citizen, String> roomColumn;

    @FXML private TextField txtSearch;
    @FXML private Label lblStatus;

    private ObservableList<Citizen> citizenList = FXCollections.observableArrayList();
    private final CitizenController controller = new CitizenController();

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        fullnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullname()));
        dobColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateofbirth()));
        placeOfBirthColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPlaceofbirth()));
        occupationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOccupation()));
        nationalIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNationalid()));
        statusColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getResidencyStatus()));
        roomColumn.setCellValueFactory(cellData -> {
            Room r = cellData.getValue().getRoomid();
            return new SimpleStringProperty(r == null ? "" : String.valueOf(r.getRoomnumber()));
        });

        citizenList.setAll(controller.getAllCitizens());
        citizenTable.setItems(citizenList);
        citizenTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) { // click đôi, đổi thành 1 nếu muốn click đơn
                Citizen selected = citizenTable.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    Citizen updatedCitizen = showCitizenForm(selected);
                    if (updatedCitizen != null) {
                        citizenTable.refresh();
                    }
                }
            }
        });
    }

    @FXML
    private void onSearch() {
        String keyword = txtSearch.getText().trim().toLowerCase();
        if (keyword.isEmpty()) {
            citizenTable.setItems(citizenList);
            return;
        }
        ObservableList<Citizen> filtered = FXCollections.observableArrayList();
        for (Citizen c : citizenList) {
            if (c.getFullname().toLowerCase().contains(keyword)) {
                filtered.add(c);
            }
        }
        citizenTable.setItems(filtered);
    }

    @FXML
    private void onAdd() {
        Citizen newCitizen = showCitizenForm(null);
        if (newCitizen != null) {
            citizenList.setAll(controller.getAllCitizens());
            citizenTable.setItems(citizenList);
            citizenTable.refresh();
        }
    }

    @FXML
    private void onEdit() {
        Citizen selected = citizenTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }
        Citizen updatedCitizen = showCitizenForm(selected);
        if (updatedCitizen != null) {
            citizenTable.refresh();
        }
    }

    @FXML
    private void onDelete() {
        Citizen selected = citizenTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Hãy chọn công dân để xóa").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa công dân này?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    controller.deleteCitizen(selected);

                    citizenList.setAll(controller.getAllCitizens());
                    citizenTable.setItems(citizenList);
                    citizenTable.refresh();

                    new Alert(Alert.AlertType.INFORMATION, "Đã xoá công dân thành công.").showAndWait();
                } catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
                }
            }
        });
    }


    private Citizen showCitizenForm(Citizen citizen) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CitizenForm.fxml"));
            Parent root = loader.load();
            CitizenFormViewController formController = loader.getController();
            formController.setCitizen(citizen);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            if (formController.isSaveClicked()) {
                return formController.getCitizen();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

}
