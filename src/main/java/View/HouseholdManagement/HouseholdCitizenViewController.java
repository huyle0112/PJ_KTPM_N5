package view.HouseholdManagement;

import controller.CitizenController;
import controller.ResidentController;
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
import view.CitizenManagement.CitizenFormViewController;
import view.HouseholdManagement.AddCitizenToHouseholdController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class HouseholdCitizenViewController {

    @FXML private TableView<Citizen> citizenTable;
    @FXML private TableColumn<Citizen, Integer> idColumn;
    @FXML private TableColumn<Citizen, String> fullnameColumn;
    @FXML private TableColumn<Citizen, String> relationshipToOwnerColumn;
    @FXML private TableColumn<Citizen, LocalDate> dobColumn;
    @FXML private TableColumn<Citizen, String> nationalIdColumn;

    @FXML private TextField txtSearch;
    @FXML private Label lblStatus;

    private ObservableList<Citizen> citizenList = FXCollections.observableArrayList();
    private final CitizenController controller = new CitizenController();
    private final ResidentController residentController = new ResidentController();
    private int currentHouseholdId = -1;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        fullnameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullname()));
        relationshipToOwnerColumn.setCellValueFactory(cellData -> {
            String relationship = cellData.getValue().getRelationshipToOwner();
            if (relationship.equals("Head")) {
                return new SimpleStringProperty("CHỦ HỘ");
            }
            return new SimpleStringProperty(relationship);
        });
        dobColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateofbirth()));
        nationalIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNationalid()));

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
        idColumn.setText("ID");
        fullnameColumn.setText("Họ tên");
        relationshipToOwnerColumn.setText("MQH");
        dobColumn.setText("Ngày sinh");
        nationalIdColumn.setText("CCCD");
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
        lblStatus.setText("Đã lọc theo từ khoá: " + keyword);
    }

    @FXML
    private void onChangeHead() {
        // Get list of citizens excluding current head
        Citizen currentHead = null;
        for (Citizen c : citizenList) {
            if ("Head".equalsIgnoreCase(c.getRelationshipToOwner())) {
                currentHead = c;
                break;
            }
        }
        ObservableList<Citizen> candidates = FXCollections.observableArrayList();
        for (Citizen c : citizenList) {
            if (currentHead == null || !c.getId().equals(currentHead.getId())) {
                candidates.add(c);
            }
        }
        if (candidates.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Không có công dân nào để chọn làm chủ hộ.").showAndWait();
            return;
        }
        Dialog<Citizen> dialog = new Dialog<>();
        dialog.setTitle("Đổi chủ hộ");
        dialog.setHeaderText("Chọn công dân mới làm chủ hộ");
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);
        ComboBox<Citizen> comboBox = new ComboBox<>(candidates);
        comboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Citizen c, boolean empty) {
                super.updateItem(c, empty);
                setText((empty || c == null) ? null : c.getFullname());
            }
        });
        comboBox.setButtonCell(comboBox.getCellFactory().call(null));
        dialog.getDialogPane().setContent(comboBox);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return comboBox.getValue();
            }
            return null;
        });
        dialog.showAndWait().ifPresent(newHead -> {
            if (newHead == null) return;
            try {
                residentController.changeHouseholdHead(newHead.getId(), currentHouseholdId);
                citizenList.setAll(controller.getMembersByHouseholdId(currentHouseholdId));
                citizenTable.setItems(citizenList);
                citizenTable.refresh();
                new Alert(Alert.AlertType.INFORMATION, "Đã đổi chủ hộ thành công.").showAndWait();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        });
    }

    @FXML
    private void onAdd() {
        showAddCitizenToHouseholdForm();
    }

    @FXML
    private void onEdit() {
        Citizen selected = citizenTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Hãy chọn công dân để sửa quan hệ").showAndWait();
            return;
        }
        if (selected.getRelationshipToOwner().equals("Head")) {
            new Alert(Alert.AlertType.WARNING, "Không thể sửa quan hệ của chủ hộ").showAndWait();
            return;
        }
        TextInputDialog dialog = new TextInputDialog(selected.getRelationshipToOwner());
        dialog.setTitle("Sửa quan hệ với chủ hộ");
        dialog.setHeaderText("Nhập quan hệ mới với chủ hộ cho " + selected.getFullname());
        dialog.setContentText("Quan hệ:");
        dialog.showAndWait().ifPresent(newRelation -> {
            try {
                residentController.updateRelationship(selected.getId(), currentHouseholdId, newRelation);
                citizenList.setAll(controller.getMembersByHouseholdId(currentHouseholdId));
                citizenTable.setItems(citizenList);
                citizenTable.refresh();
                new Alert(Alert.AlertType.INFORMATION, "Đã cập nhật quan hệ thành công.").showAndWait();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        });
    }

    @FXML
    private void onDelete() {
        Citizen selected = citizenTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Hãy chọn công dân để xóa").showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Bạn có chắc chắn muốn xóa công dân này khỏi hộ khẩu?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    residentController.removeCitizenFromHousehold(selected.getId(), currentHouseholdId);
                    citizenList.setAll(controller.getMembersByHouseholdId(currentHouseholdId));
                    citizenTable.setItems(citizenList);
                    citizenTable.refresh();
                    new Alert(Alert.AlertType.INFORMATION, "Đã xoá công dân khỏi hộ khẩu thành công.").showAndWait();
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
        lblStatus.setText("Đã trở về trang chủ");
    }
    
    @FXML
    private void onBack(ActionEvent event) {
    	try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/HouseholdView.fxml"));
            Parent citizenView = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(citizenView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lblStatus.setText("Đã trở về danh sách hộ khẩu");
    }

    public void showCitizensOfHousehold(int householdId) {
        this.currentHouseholdId = householdId;
        citizenList.setAll(controller.getMembersByHouseholdId(householdId));
        citizenTable.setItems(citizenList);
    }

    private void showAddCitizenToHouseholdForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddCitizenToHousehold.fxml"));
            Parent root = loader.load();
            AddCitizenToHouseholdController formController = loader.getController();
            formController.setHouseholdId(currentHouseholdId);
            formController.setOnCitizenAdded(() -> {
                // Refresh the list after adding
                citizenList.setAll(controller.getMembersByHouseholdId(currentHouseholdId));
                citizenTable.setItems(citizenList);
                citizenTable.refresh();
            });

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
