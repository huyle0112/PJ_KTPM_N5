package view;

import application.SceneManager;
import controller.ResidentChargeController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ResidentCharge;
import org.hibernate.Session;
import org.w3c.dom.events.Event;
import service.GenericDAO;
import service.HibernateUtil;

import java.io.IOException;
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

    @FXML
    private Button chargeButton;

    @FXML
    private Label moneyValueLabel;

    private ResidentCharge residentCharge;
    private ResidentChargeController residentChargeController;
    private SceneManager sceneManager;
    private Session session = HibernateUtil.getSessionFactory().openSession();

    public ChargeDetailsViewController(ResidentCharge residentCharge){
        this.residentCharge = residentCharge;
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        residentChargeController = new ResidentChargeController();
        sceneManager = new SceneManager();
        populateFields();
        if (residentCharge.getTypeOfCharge().equals("Voluntary")) {
            moneyValueLabel.setVisible(false);
            moneyValueLabel.setManaged(false);
            moneyLabel.setVisible(false);
            moneyLabel.setManaged(false);
        }
        updateChargeButton.setOnAction(e -> handleUpdate());
        deleteButton.setOnAction(e -> handleDelete());
        cancelButton.setOnAction(e -> exitScene());
        chargeButton.setOnAction(e -> handleCharge());
    }

    private void populateFields() {
        residentCharge = residentChargeController.findById(residentCharge.getId());
        if (residentCharge == null) return;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Instant createdInstant = residentCharge.getCreatedDate();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(createdInstant, ZoneId.systemDefault());

        nameTextField.setText(residentCharge.getName());
        typeLabel.setText(residentCharge.getTypeOfCharge());
        createDateLabel.setText(localDateTime.format(fmt));
        moneyValueLabel.setText(String.valueOf(residentCharge.getMoney()));
        descriptionTextField.setText(residentCharge.getDescription());
        sumChargeLabel.setText(String.valueOf(residentCharge.getSumCharge()));
        paidCountLabel.setText(String.valueOf(residentCharge.getHouseholdsPaidCount()));

        // Danh sách hộ đã nộp
        listPaidVBox.getChildren().clear();
        residentCharge.getPaidHouseholdList().forEach(hh -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaidHouseholdCard.fxml"));
                VBox card = loader.load();
                PaidHouseholdCardController controller = loader.getController();
                controller.setData(hh);
                listPaidVBox.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    private void handleUpdate() {
        String name = nameTextField.getText().trim();
        String description = descriptionTextField.getText().trim();

        if (name.isEmpty() || description.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thiếu thông tin");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đầy đủ tên và mô tả khoản thu.");
            alert.showAndWait();
            return;
        }

        residentCharge.setName(name);
        residentCharge.setDescription(description);
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

    private void handleCharge(){
        ChargeViewController controller = new ChargeViewController(residentCharge);
        controller.setParentController(this);
        sceneManager.showViewWithController("/ChargeView.fxml", controller, "Thu phí");
    }

    private void exitScene() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        sceneManager.showViewWithOutController("/ResidentChargeView.fxml", "Quản lý khoản thu");
    }

    @Override
    public void reload(){
        this.populateFields();
    }
}



