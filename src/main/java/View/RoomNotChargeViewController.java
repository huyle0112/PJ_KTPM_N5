package view;

import application.SceneManager;
import controller.ChargeController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.ResidentCharge;
import model.Room;
import org.hibernate.Session;
import service.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

public class RoomNotChargeViewController implements BlueMoonViewController {

    @FXML
    private TableView<Room> unpaidHouseholdTable;

    @FXML
    private TableColumn<Room, String> roomColumn;

    @FXML
    private TableColumn<Room, Void> feeColumn;

    private ResidentCharge residentCharge;

    private ChargeController chargeController;

    private SceneManager sceneManager;

    private ChargeDetailsViewController chargeDetailsViewController;

    public RoomNotChargeViewController(ResidentCharge residentCharge){
        this.residentCharge = residentCharge;
    }

    public void setParentController(ChargeDetailsViewController controller){
        this.chargeDetailsViewController = controller;
    }

    @FXML
    public void initialize() {
        // Cột phòng hiển thị roomnumber

        sceneManager = new SceneManager();
        roomColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getRoomnumber()));

        // Cột Thu phí có button "Thu phí"
        feeColumn.setCellFactory(new Callback<TableColumn<Room, Void>, TableCell<Room, Void>>() {
            @Override
            public TableCell<Room, Void> call(final TableColumn<Room, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Thu phí");
                    private final HBox container = new HBox(btn);

                    {
                        btn.setOnAction(event -> {
                            Room room = getTableView().getItems().get(getIndex());
                            handleCharge(room.getRoomnumber());
                        });
                        btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

                        // Căn giữa nút trong HBox
                        container.setAlignment(Pos.CENTER);
                        container.setPrefWidth(Double.MAX_VALUE);
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(container);
                        }
                    }
                };
            }
        });


        // Load dữ liệu mẫu hoặc từ DB
        loadUnpaidRooms();
    }

    private void loadUnpaidRooms() {
        // Đây là ví dụ dữ liệu mẫu, bạn có thể thay bằng truy vấn từ Hibernate
        List<Room> unpaidRooms = new ArrayList<>();
        try(Session session = HibernateUtil.getSessionFactory().openSession()){
            chargeController = new ChargeController(session);
            unpaidRooms = chargeController.getRoomsNotCharge(residentCharge.getId());
        } catch ( Exception e){
            e.printStackTrace();
        }
        unpaidHouseholdTable.getItems().setAll(unpaidRooms);
    }

    private void handleCharge(String room){
        ChargeViewController chargeViewController = new ChargeViewController(residentCharge);
        chargeViewController.setParentController(chargeDetailsViewController);
        chargeViewController.setPreselectedRoom(room);
        sceneManager.showViewWithController("/ChargeView.fxml", chargeViewController, "Thu phí");
        Stage stage = (Stage) unpaidHouseholdTable.getScene().getWindow();
        stage.close();
    }

    @Override
    public void reload() {}
}
