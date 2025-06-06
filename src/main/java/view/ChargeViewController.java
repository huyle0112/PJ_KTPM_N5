package view;

import application.SceneManager;
import controller.ChargeController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.PaidHousehold;
import model.ResidentCharge;
import model.Room;
import org.hibernate.Session;
import service.HibernateUtil;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ChargeViewController implements BlueMoonViewController {

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

    @FXML
    private Label titleLabel;

    @FXML
    private Label amountM2Label;

    @FXML
    private Label moneyM2Label;

    @FXML
    private Label areaLabel;

    @FXML
    private Label areaValueLabel;

    private String preselectedRoom;

    public void setPreselectedRoom(String roomNumber) {
        this.preselectedRoom = roomNumber;
    }

    private final java.util.Map<String, Room> roomMap = new java.util.HashMap<>();


    private BlueMoonViewController blueMoonViewController;
    private SceneManager sceneManager;
    private ChargeController chargeController;
    private Session session = HibernateUtil.getSessionFactory().openSession();
    private ResidentCharge charge;

    public ChargeViewController(ResidentCharge charge){
        this.charge = charge;
    }

    public void setParentController(BlueMoonViewController controller){
        this.blueMoonViewController = controller;
    }
    @FXML
    public void initialize() {
        sceneManager = new SceneManager();
        chargeController = new ChargeController(session);
        titleLabel.setText(charge.getName());
        if(charge.getTypeOfCharge().equals("Mandatory")){
            amountM2Label.setText(String.valueOf(charge.getMoney()));
        }else{
            amountM2Label.setVisible(false);
            amountM2Label.setManaged(false);
            moneyM2Label.setManaged(false);
            moneyM2Label.setVisible(false);
            areaLabel.setVisible(false);
            areaLabel.setManaged(false);
            areaValueLabel.setManaged(false);
            areaValueLabel.setVisible(false);
        }
        List<Room> rooms = new ArrayList<>();
        ObservableList<String> roomNumbers = FXCollections.observableArrayList();
        if(charge.getTypeOfCharge().equals("Mandatory")){
            rooms = chargeController.getRoomsNotCharge(charge.getId());
        }else {
            rooms = chargeController.getRooms();
        }
        for (Room room : rooms){
            roomNumbers.add(room.getRoomnumber());
            roomMap.put(room.getRoomnumber(), room);
        }
        roomComboBox.setValue("Chọn số phòng");
        roomComboBox.setItems(roomNumbers);
        roomComboBox.setVisibleRowCount(3);
        if (preselectedRoom != null && roomMap.containsKey(preselectedRoom)) {
            roomComboBox.setValue(preselectedRoom);
            Room selectedRoom = roomMap.get(preselectedRoom);
            if (selectedRoom != null) {
                areaValueLabel.setText(String.valueOf(selectedRoom.getArea()) + " m2");
            } else {
                areaValueLabel.setText("");
            }
            if(charge.getTypeOfCharge().equals("Mandatory")){
                moneyTextField.setText(String.valueOf(selectedRoom.getArea() * charge.getMoney()));
            }
        }
        addChargeButton.setOnAction(this::handleAdd);
        cancelButton.setOnAction(this::handleCancel);
        roomComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Room selectedRoom = roomMap.get(newValue);
            if (selectedRoom != null) {
                areaValueLabel.setText(String.valueOf(selectedRoom.getArea()) + " m2");
            } else {
                areaValueLabel.setText("");
            }
            if(charge.getTypeOfCharge().equals("Mandatory")){
                moneyTextField.setText(String.valueOf(selectedRoom.getArea() * charge.getMoney()));
            }
        });

    }

    private void handleAdd(ActionEvent event){
        // Kiểm tra dữ liệu đầu vào
        String selectedRoom = roomComboBox.getValue();
        String name = nameTextField.getText();
        String moneyStr = moneyTextField.getText();

        if (selectedRoom == null || selectedRoom.equals("Chọn số phòng")
                || name == null || name.trim().isEmpty()
                || moneyStr == null || moneyStr.trim().isEmpty()) {

            // Hiển thị cảnh báo
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Thiếu thông tin");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn phòng, nhập tên và số tiền.");
            alert.showAndWait();
            return;
        }

        int money;

        try {
            money = Integer.parseInt(moneyStr);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi định dạng");
            alert.setHeaderText(null);
            alert.setContentText("Số tiền phải là số nguyên hợp lệ.");
            alert.showAndWait();
            return;
        }

        // Lấy phòng
        Room roomInput = null;
        List<Room> rooms = chargeController.getRooms();
        for (Room room : rooms){
            if(selectedRoom.equals(room.getRoomnumber())){
                roomInput = room;
                break;
            }
        }

        if (roomInput == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Không tìm thấy phòng");
            alert.setHeaderText(null);
            alert.setContentText("Không tìm thấy thông tin phòng đã chọn.");
            alert.showAndWait();
            return;
        }

        // Tạo đối tượng PaidHousehold
        PaidHousehold paidHousehold = new PaidHousehold();
        paidHousehold.setFullname(name);
        paidHousehold.setAmountPaid(money);
        paidHousehold.setPaidDate(Instant.now());
        paidHousehold.setCharge(charge);
        paidHousehold.setRoomid(roomInput);

        // Gửi dữ liệu và thoát
        chargeController.charge(paidHousehold);
        exitScene();
    }


    private void handleCancel(ActionEvent event){
        exitScene();
    }

    private void exitScene() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        reload();
    }

    @Override
    public void reload(){
        blueMoonViewController.reload();
    }
}