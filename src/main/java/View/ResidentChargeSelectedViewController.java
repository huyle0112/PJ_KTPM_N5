package view;

import application.SceneManager;
import controller.ResidentChargeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import model.ResidentCharge;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResidentChargeSelectedViewController implements Initializable, BlueMoonViewController {

    @FXML
    private ComboBox<String> residentChargesComboBox;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private ResidentChargeController controller;

    private SceneManager sceneManager;


    private BlueMoonViewController blueMoonViewController;

    public void setParentController(BlueMoonViewController controller){
        this.blueMoonViewController = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        controller = new ResidentChargeController();
        sceneManager = new SceneManager();
        ObservableList<String> residentCharges = FXCollections.observableArrayList();
        List<ResidentCharge> chargeList = controller.requestList();
        for (ResidentCharge charge : chargeList){
            residentCharges.add(charge.getName());
        }
        residentChargesComboBox.setEditable(false);
        residentChargesComboBox.setItems(residentCharges);
        confirmButton.setOnAction(this::handleConfirmAction);
        cancelButton.setOnAction(this::handleCancelAction);
    }

    @FXML
    private void handleConfirmAction(ActionEvent event) {
        String selectedCharge = residentChargesComboBox.getValue();
        if (selectedCharge == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một khoản thu!");
            alert.showAndWait();
            return;
        }
        List<ResidentCharge> chargeList = controller.requestList();
        ResidentCharge selected = new ResidentCharge();
        for (ResidentCharge charge : chargeList){
            if(charge.getName().equals(selectedCharge)){
                selected = charge;
                break;
            }
        }
        ChargeViewController chargeViewController = new ChargeViewController(selected);
        chargeViewController.setParentController(blueMoonViewController);
        sceneManager.showViewWithController("/ChargeView.fxml", chargeViewController, "Thu phí");
        handleCancelAction(event);
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void reload() {}
}
