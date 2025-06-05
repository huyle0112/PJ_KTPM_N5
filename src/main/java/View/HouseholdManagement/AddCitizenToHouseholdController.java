package view.HouseholdManagement;

import controller.CitizenController;
import controller.ResidentController;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Citizen;
import model.Household;
import model.Residence;
import model.ResidenceId;
import service.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class AddCitizenToHouseholdController {
    @FXML private ComboBox<Citizen> citizenComboBox;
    @FXML private TextField relationField;

    private int householdId;
    private Runnable onCitizenAdded;
    private final CitizenController citizenController = new CitizenController();
    private final ResidentController residentController = new ResidentController();

    public void setHouseholdId(int householdId) {
        this.householdId = householdId;
        loadCitizens();
    }

    public void setOnCitizenAdded(Runnable callback) {
        this.onCitizenAdded = callback;
    }

    private void loadCitizens() {
        List<Citizen> citizens = citizenController.getCitizensNotInHousehold();
        citizenComboBox.setItems(FXCollections.observableArrayList(citizens));
        citizenComboBox.setCellFactory(param -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Citizen citizen, boolean empty) {
                super.updateItem(citizen, empty);
                setText((empty || citizen == null) ? null : citizen.getFullname());
            }
        });
        citizenComboBox.setButtonCell(citizenComboBox.getCellFactory().call(null));
    }

    @FXML
    private void onSave() {
        Citizen selected = citizenComboBox.getValue();
        String relation = relationField.getText().trim();
        if (selected == null || relation.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Vui lòng chọn công dân và nhập quan hệ với chủ hộ.").showAndWait();
            return;
        }
        try {
            residentController.addCitizenToHousehold(selected.getId(), householdId, relation);
            if (onCitizenAdded != null) onCitizenAdded.run();
            close();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) citizenComboBox.getScene().getWindow();
        stage.close();
    }
} 