package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.PaidHousehold;
import model.ResidentCharge;
import model.Room;
import org.hibernate.Session;
import service.HibernateUtil;
import service.RoomDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class PaidHouseholdListViewController implements Initializable, BlueMoonViewController {
    @FXML
    private TableView<PaidHousehold> paidHouseholdTable;
    @FXML
    private TableColumn<PaidHousehold, String> roomColumn;
    @FXML
    private TableColumn<PaidHousehold, String> payerColumn;
    @FXML
    private TableColumn<PaidHousehold, String> dateColumn;
    @FXML
    private TableColumn<PaidHousehold, String> amountColumn;

    private ResidentCharge residentCharge;

    public void setResidentCharge(ResidentCharge residentCharge){
        this.residentCharge = residentCharge;
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        roomColumn.setCellValueFactory(cellData -> {
            String roomName = cellData.getValue().getRoomid().getRoomnumber();
            return new SimpleStringProperty(roomName);
        });

        payerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullname()));
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPaidDate().toString()));
        amountColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAmountPaid() + " VND"));
        ObservableList<PaidHousehold> data = FXCollections.observableArrayList(residentCharge.getPaidHouseholdList());
        paidHouseholdTable.setItems(data);
    }

    @Override
    public void reload() {}
}
