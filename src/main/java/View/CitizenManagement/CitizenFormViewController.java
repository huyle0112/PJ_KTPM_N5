package view.CitizenManagement;

import controller.CitizenController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Citizen;
import model.Citizen.ResidencyStatus;
import model.Room;

import java.time.LocalDate;
public class CitizenFormViewController {
    @FXML private TextField nameField;
    @FXML private TextField jobField;
    @FXML private TextField placeOfBirthField;
    @FXML private TextField occupationField;
    @FXML private TextField nationalIdField;
    @FXML private ComboBox<ResidencyStatus> statusCombo;
    @FXML private TextField roomIdField;
    @FXML private GridPane residenceDetailsBox;
    @FXML private TextField relationToOwnerField;
    @FXML private Label relationLabel;
    @FXML private CheckBox isHeadCheckbox;
    @FXML private TextField householdIdField;
    private Citizen citizen;
    private boolean saveClicked = false;

    private final CitizenController citizenController = new CitizenController();

    @FXML
    public void initialize() {
        statusCombo.getItems().setAll(ResidencyStatus.values());

        statusCombo.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean show = newVal == ResidencyStatus.Away || newVal == ResidencyStatus.Permanent;
            residenceDetailsBox.setVisible(show);
            residenceDetailsBox.setManaged(show);

            Platform.runLater(() -> {
                Stage stage = (Stage) nameField.getScene().getWindow();
                if (stage != null) {
                    stage.sizeToScene();
                }
            });
        });


        isHeadCheckbox.selectedProperty().addListener((obs, oldVal, isHead) -> {
            relationLabel.setVisible(!isHead);
            relationToOwnerField.setVisible(!isHead);
            relationToOwnerField.setManaged(!isHead);
            Stage stage = (Stage) isHeadCheckbox.getScene().getWindow();
            stage.sizeToScene();
        });

    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
        if (citizen != null) {
            nameField.setText(citizen.getFullname());
            jobField.setText(citizen.getOccupation());
            placeOfBirthField.setText(citizen.getPlaceofbirth());
            occupationField.setText(citizen.getOccupation());
            nationalIdField.setText(citizen.getNationalid());
            statusCombo.setValue(citizen.getResidencyStatus());
            roomIdField.setText(String.valueOf(citizen.getRoomid()));
        }
    }

    public Citizen getCitizen() { return citizen; }

    public boolean isSaveClicked() { return saveClicked; }

    @FXML
    private void onSave() {
        try {
            if (citizen == null) citizen = new Citizen();
            citizen.setFullname(nameField.getText());
            citizen.setDateofbirth(LocalDate.parse(jobField.getText()));
            citizen.setPlaceofbirth(placeOfBirthField.getText());
            citizen.setOccupation(occupationField.getText());
            citizen.setNationalid(nationalIdField.getText());
            citizen.setResidencyStatus(statusCombo.getValue());

            if (!roomIdField.getText().isEmpty()) {
                Room room = new Room();
                room.setId(Integer.parseInt(roomIdField.getText()));
                citizen.setRoomid(room);
            }

            ResidencyStatus status = statusCombo.getValue();
            Integer householdId = null;
            String relation = null;
            boolean isHead = false;

            if (status == ResidencyStatus.Away || status == ResidencyStatus.Permanent) {
                String householdIdStr = householdIdField.getText();
                if (householdIdStr == null || householdIdStr.isEmpty())
                    throw new IllegalArgumentException("Vui lòng nhập ID hộ khẩu!");
                householdId = Integer.parseInt(householdIdStr);

                isHead = isHeadCheckbox.isSelected();
                relation = isHead ? "Head" : relationToOwnerField.getText();
            }

            boolean ok = citizenController.addCitizen(citizen, householdId, relation, isHead, status);
            if (!ok) return;

            saveClicked = true;
            ((Stage) nameField.getScene().getWindow()).close();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Lỗi nhập liệu: " + ex.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onCancel() {
        ((Stage) nameField.getScene().getWindow()).close();
    }
}
