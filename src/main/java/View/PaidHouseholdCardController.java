package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.PaidHousehold;
import model.Room;
import org.hibernate.Session;
import service.HibernateUtil;
import service.RoomDAO;

public class PaidHouseholdCardController {

    @FXML private Label roomLabel;
    @FXML private Label payerLabel;
    @FXML private Label dateLabel;
    @FXML private Label amountLabel;
    @FXML private VBox cardBox;

    public void setData(PaidHousehold household) {
        Room room = household.getRoomid();
        String roomName;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            RoomDAO roomDAO = new RoomDAO(session);
            roomName = roomDAO.findById(room.getId()).getRoomnumber();
        }
        roomLabel.setText("Phòng: " + roomName);
        payerLabel.setText("Người nộp: " + household.getFullname());
        dateLabel.setText("Ngày nộp: " + household.getPaidDate());
        amountLabel.setText(household.getAmountPaid() + " VND");
    }
}
