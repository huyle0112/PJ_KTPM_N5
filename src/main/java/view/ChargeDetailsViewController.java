package view;

import application.SceneManager;
import controller.ResidentChargeController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ResidentCharge;
import org.hibernate.Session;
import service.HibernateUtil;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChargeDetailsViewController implements Initializable, BlueMoonViewController{
    @FXML
    private TextField nameTextField;

    @FXML
    private Label typeLabel;

    @FXML
    private Label createDateLabel;

    @FXML
    private Label moneyLabel;

    @FXML
    private TextField moneyTextField;

    @FXML
    private TextField descriptionTextField;

    @FXML
    private Label sumChargeLabel;

    @FXML
    private Label paidCountLabel;

    @FXML
    private VBox listPaidVBox;

    @FXML
    private Button updateChargeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    private ResidentCharge residentCharge;
    private ResidentChargeController residentChargeController;
    private SceneManager sceneManager;
    private Session session = HibernateUtil.getSessionFactory().openSession();

    public ChargeDetailsViewController(ResidentCharge residentCharge){
        this.residentCharge = residentCharge;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        residentChargeController = new ResidentChargeController(session);
        sceneManager = new SceneManager();
        populateFields();
        if (residentCharge.getTypeOfCharge().equals("voluntary")) {
            moneyTextField.setVisible(false);
            moneyTextField.setManaged(false);
            moneyLabel.setVisible(false);
            moneyLabel.setManaged(false);
        }
        updateChargeButton.setOnAction(e -> handleUpdate());
        deleteButton.setOnAction(e -> handleDelete());
        cancelButton.setOnAction(e -> exitScene());
    }

    private void populateFields() {
        if (residentCharge == null) return;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Instant createdInstant = residentCharge.getCreatedDate();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(createdInstant, ZoneId.systemDefault());

        nameTextField.setText(residentCharge.getName());
        typeLabel.setText(residentCharge.getTypeOfCharge());
        createDateLabel.setText(localDateTime.format(fmt));
        moneyTextField.setText(String.valueOf(residentCharge.getMoney()));
        descriptionTextField.setText(residentCharge.getDescription());
        sumChargeLabel.setText(String.valueOf(residentCharge.getSumCharge()));
        paidCountLabel.setText(String.valueOf(residentCharge.getHouseholdsPaidCount()));

        // Danh sách hộ đã nộp
        listPaidVBox.getChildren().clear();
        residentCharge.getPaidHouseholdList().forEach(hh -> {
            Label lbl = new Label(hh.getFullname() + "  –  " + hh.getAmountPaid() + " VND");
            listPaidVBox.getChildren().add(lbl);
        });
    }

    private void handleUpdate() {
        residentCharge.setName(nameTextField.getText());
        if(residentCharge.getTypeOfCharge().equals("mandatory")) {
            residentCharge.setMoney(Integer.parseInt(moneyTextField.getText()));
        }
        residentCharge.setDescription(descriptionTextField.getText());
        residentChargeController.update(residentCharge);
        exitScene();

    }

    private void handleDelete() {
        if (residentCharge != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận xóa");
            alert.setHeaderText("Bạn có chắc muốn xóa khoản thu này?");
            alert.setContentText("Thao tác này không thể hoàn tác.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                residentChargeController.delete(residentCharge);
                exitScene();
            }
        }
    }
    private void exitScene() {
        session.close();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        sceneManager.showViewWithOutController("/ResidentChargeView.fxml", "Quản lý khoản thu");
    }
}



