package view;

import application.SceneManager;
import controller.ChargeController;
import controller.ResidentChargeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.PaidHousehold;
import model.ResidentCharge;
import model.Room;
import org.hibernate.Session;
import service.GenericDAO;
import service.HibernateUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChargeViewController implements BlueMoonViewController {

    @FXML
    private ComboBox<String> residentChargesComboBox;

    @FXML
    private ComboBox<String> roomComboBox;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField moneyTextField;

    @FXML
    private Button addChargeButton;

    @FXML
    private Button cancelButton;

    private ResidentChargeViewController residentChargeViewController;
    private ObservableList<String> residentCharges = FXCollections.observableArrayList();
    private ResidentChargeController residentChargeController;
    private SceneManager sceneManager;
    private ChargeController chargeController;
    private Session session = HibernateUtil.getSessionFactory().openSession();

    public void setParentController(ResidentChargeViewController parentController) {
        this.residentChargeViewController = parentController;
    }

    @FXML
    public void initialize() {
        sceneManager = new SceneManager();
        residentChargeController = new ResidentChargeController();
        chargeController = new ChargeController(session);
        List<ResidentCharge> list = residentChargeController.requestList();

        for (ResidentCharge charge : list) {
            residentCharges.add(charge.getName());
        }
        GenericDAO<Room> roomDAO = new GenericDAO<>(Room.class, session);
        List<Room> rooms = roomDAO.findAll();
        List<String> roomNumbers = new ArrayList<>();
        for (Room room : rooms){
            roomNumbers.add(room.getRoomnumber());
        }
        roomComboBox.setValue("Chọn số phòng");
        residentChargesComboBox.setValue("Chọn khoản thu");
        residentChargesComboBox.setEditable(false);
        residentChargesComboBox.setItems(residentCharges);
        addChargeButton.setOnAction(this::handleAdd);
        cancelButton.setOnAction(this::handleCancel);
    }

    private void handleAdd(ActionEvent event){
        GenericDAO<Room> roomDAO = new GenericDAO<>(Room.class, session);
        Room roomInput = new Room();
        List<Room> rooms = roomDAO.findAll();
        for (Room room : rooms){
            if(roomComboBox.getValue().equals(room.getRoomnumber())){
                roomInput = room;
            }
        }
        String name = nameTextField.getText();
        int money = Integer.parseInt(moneyTextField.getText());
        ResidentCharge residentCharge = new ResidentCharge();
        List<ResidentCharge> list = residentChargeController.requestList();
        for(ResidentCharge charge : list){
            if(residentChargesComboBox.getValue().equals(charge.getName())){
                residentCharge = charge;
            }
        }
        Instant paidDate = Instant.now();
        PaidHousehold paidHousehold = new PaidHousehold();
        paidHousehold.setFullname(name);
        paidHousehold.setAmountPaid(money);
        paidHousehold.setPaidDate(paidDate);
        paidHousehold.setCharge(residentCharge);
        paidHousehold.setRoomid(roomInput);
        chargeController.charge(paidHousehold);
        exitScene();
    }

    private void handleCancel(ActionEvent event){
        exitScene();
    }

    private void exitScene() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        residentChargeViewController.loadData();
    }
}