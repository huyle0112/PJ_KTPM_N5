package view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.ResidentCharge;

public class ResidentChargeCardController {

    @FXML private Label nameLabel;
    @FXML private Label typeLabel;
    @FXML private Label createdDateLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label moneyLabel;
    @FXML private Label sumLabel;
    @FXML private Label countLabel;

    private ResidentCharge charge;

    public void setData(ResidentCharge charge) {
        this.charge = charge;
        nameLabel.setText("Tên: " + charge.getName());
        typeLabel.setText("Loại: " + charge.getTypeOfCharge());
        createdDateLabel.setText("Ngày tạo: " + charge.getCreatedDate());
        descriptionLabel.setText("Mô tả: " + charge.getDescription());
        sumLabel.setText("Tổng tiền đã thu: " + charge.getSumCharge());
        countLabel.setText("Số hộ đã thu: " + charge.getHouseholdsPaidCount());

        if ("mandatory".equals(charge.getTypeOfCharge())) {
            moneyLabel.setText("Số tiền/m2: " + charge.getMoney());
        } else {
            moneyLabel.setVisible(false);
            moneyLabel.setManaged(false);
        }
    }


}
