package view;

import application.SceneManager;
import controller.ResidentChargeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.ResidentCharge;
import org.hibernate.Session;
import service.HibernateUtil;

import java.net.URL;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.UUID;

public class AddChargeViewController implements Initializable, BlueMoonViewController {

    @FXML
    private TextField nameTextField;

    @FXML
    private ChoiceBox<String> typeChargeChoiceBox;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private TextField moneyTextField;

    @FXML
    private Label moneyLabel;

    @FXML
    private Button addChargeButton;

    @FXML
    private Button cancelButton;

    private ResidentChargeController residentChargeController;

    private SceneManager sceneManager;

    private Session session;

    private ResidentChargeViewController residentChargeViewController;

    public void setParentController(ResidentChargeViewController parentController) {
        this.residentChargeViewController = parentController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.session = HibernateUtil.getSessionFactory().openSession();
        residentChargeController = new ResidentChargeController();
        sceneManager = new SceneManager();
        typeChargeChoiceBox.getItems().addAll("Mandatory", "Voluntary");
        typeChargeChoiceBox.setValue("Mandatory");

        typeChargeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Voluntary".equals(newVal)) {
                moneyTextField.setVisible(false);
                moneyTextField.setManaged(false);
                moneyLabel.setVisible(false);
                moneyLabel.setManaged(false);
            } else {
                moneyTextField.setVisible(true);
                moneyTextField.setManaged(true);
                moneyLabel.setVisible(true);
                moneyLabel.setManaged(true);
            }
        });
        addChargeButton.setOnAction(this::handleAdd);
        cancelButton.setOnAction(this::handleCancel);
    }

    private void handleAdd(ActionEvent evt){
        String name = nameTextField.getText().trim();
        String type = typeChargeChoiceBox.getValue();
        String description = descriptionTextField.getText().trim();
        String moneyStr = moneyTextField.getText().trim();

        // Kiểm tra trường rỗng
        if (name.isEmpty() || description.isEmpty()) {
            showAlert("Thiếu thông tin", "Vui lòng nhập đầy đủ tên và mô tả khoản thu.");
            return;
        }

        // Kiểm tra loại và số tiền
        int money = 0;
        if ("Mandatory".equals(type)) {
            if (moneyStr.isEmpty()) {
                showAlert("Thiếu số tiền", "Khoản thu bắt buộc cần có số tiền.");
                return;
            }
            try {
                money = Integer.parseInt(moneyStr);
                if (money <= 0) {
                    showAlert("Số tiền không hợp lệ", "Số tiền phải lớn hơn 0.");
                    return;
                }
            } catch (NumberFormatException e) {
                showAlert("Lỗi định dạng", "Số tiền phải là số nguyên.");
                return;
            }
        }

        Instant createdDate = Instant.now();
        ResidentCharge residentCharge = new ResidentCharge();
        residentCharge.setId(UUID.randomUUID());
        residentCharge.setName(name);
        residentCharge.setTypeOfCharge(type);
        residentCharge.setDescription(description);
        residentCharge.setCreatedDate(createdDate);
        residentCharge.setMoney(money);
        residentCharge.setSumCharge(0);
        residentCharge.setHouseholdsPaidCount(0);
        residentCharge.setInComplete(true);

        residentChargeController.create(residentCharge);
        exitScene();
    }


    private void handleCancel(ActionEvent evt){
       exitScene();
    }

    private void exitScene(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        reload();
    }

    @Override
    public void reload(){residentChargeViewController.reload();};

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
