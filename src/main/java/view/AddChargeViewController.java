package view;

import application.SceneManager;
import controller.ResidentChargeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.session = HibernateUtil.getSessionFactory().openSession();
        residentChargeController = new ResidentChargeController(session);
        sceneManager = new SceneManager();
        typeChargeChoiceBox.getItems().addAll("Bắt buộc", "Tự nguyện");
        typeChargeChoiceBox.setValue("Bắt buộc");

        typeChargeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Tự nguyện".equals(newVal)) {
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
        String name = nameTextField.getText();
        String type = typeChargeChoiceBox.getValue();
        String description = descriptionTextField.getText();
        int money;
        if(moneyTextField.getText().isEmpty()){
            money = 0;
        }else {
            money = Integer.parseInt(moneyTextField.getText());
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
        session.close();
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        sceneManager.showViewWithOutController("/ResidentChargeView.fxml", "Quản lý khoản thu");
    }
}
