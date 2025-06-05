package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import model.ResidentCharge;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
public class ResidentChargeCardController {

    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private Label createdDateLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label moneyLabel;
    @FXML private Label sumLabel;
    @FXML private Label countLabel;
    @FXML private HBox titleBox;
    @FXML private HBox rightTitleBox;
    @FXML private HBox leftTitleBox;

    private ResidentCharge charge;

    public void setData(ResidentCharge charge) {
        this.charge = charge;

        // In đậm và tăng kích thước chữ cho tên
        nameLabel.setText(charge.getName());

        typeLabel.setText(charge.getTypeOfCharge());

        rightTitleBox.prefWidthProperty().bind(titleBox.widthProperty().multiply(0.5));
        leftTitleBox.prefWidthProperty().bind(titleBox.widthProperty().multiply(0.5));

        createdDateLabel.setText("Ngày tạo: " + charge.getCreatedDate());
        descriptionLabel.setText("Mô tả: " + charge.getDescription());

        // Tổng tiền đã thu - in đậm và tăng size
        sumLabel.setText("Tổng tiền đã thu: " + charge.getSumCharge());
        sumLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        sumLabel.setStyle("-fx-text-fill: #27ae60;");

        // Số hộ đã nộp - in đậm và tăng size
        countLabel.setText("Số hộ đã thu: " + charge.getHouseholdsPaidCount());
        countLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
        countLabel.setStyle("-fx-text-fill: #2980b9;");

        if ("mandatory".equals(charge.getTypeOfCharge())) {
            moneyLabel.setText("Số tiền/m2: " + charge.getMoney());
            moneyLabel.setVisible(true);
            moneyLabel.setManaged(true);
        } else {
            moneyLabel.setVisible(false);
            moneyLabel.setManaged(false);
        }
    }


}
