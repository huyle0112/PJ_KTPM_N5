package view.CitizenManagement;

import controller.CitizenController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Citizen;
import model.Citizen.ResidencyStatus;
import model.Room;

import java.time.LocalDate;
public class CitizenFormViewController {
    @FXML private TextField fullnameField;
    @FXML private TextField dobField;
    @FXML private TextField placeOfBirthField;
    @FXML private TextField occupationField;
    @FXML private TextField nationalIdField;
    @FXML private ComboBox<ResidencyStatus> statusCombo;
    @FXML private TextField roomIdField;

    private Citizen citizen;
    private boolean saveClicked = false;

    private final CitizenController citizenController = new CitizenController();

    @FXML
    public void initialize() {
        statusCombo.getItems().setAll(ResidencyStatus.values());
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
        if (citizen != null) {
            fullnameField.setText(citizen.getFullname());
            dobField.setText(citizen.getDateofbirth() != null ? citizen.getDateofbirth().toString() : "");
            placeOfBirthField.setText(citizen.getPlaceofbirth());
            occupationField.setText(citizen.getOccupation());
            nationalIdField.setText(citizen.getNationalid());
            statusCombo.setValue(citizen.getResidencyStatus());
            roomIdField.setText(citizen.getRoomid() != null ? String.valueOf(citizen.getRoomid().getId()) : "");
        }
    }

    public Citizen getCitizen() { return citizen; }

    public boolean isSaveClicked() { return saveClicked; }

    @FXML
    private void onSave() {
        try {
            if (citizen == null) citizen = new Citizen();
            citizen.setFullname(fullnameField.getText());
            citizen.setDateofbirth(LocalDate.parse(dobField.getText()));
            citizen.setPlaceofbirth(placeOfBirthField.getText());
            citizen.setOccupation(occupationField.getText());
            citizen.setNationalid(nationalIdField.getText());
            citizen.setResidencyStatus(statusCombo.getValue());

            if (!roomIdField.getText().isEmpty()) {
                Room room = new Room();
                room.setId(Integer.parseInt(roomIdField.getText()));  // sửa lấy id đúng kiểu
                citizen.setRoomid(room);
            } else {
                citizen.setRoomid(null);
            }

            Integer id = citizen.getId();
            if (id == null || id == 0) {
                citizenController.addCitizen(citizen);
            } else {
                citizenController.updateCitizen(citizen);
            }

            saveClicked = true;
            ((Stage) fullnameField.getScene().getWindow()).close();
        } catch (Exception ex) {
            new Alert(Alert.AlertType.ERROR, "Lỗi nhập liệu: " + ex.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onCancel() {
        ((Stage) fullnameField.getScene().getWindow()).close();
    }
}
