package view;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

import javafx.util.Callback;
import model.Citizen;

public class ButtonCellFactory implements Callback<TableColumn<Citizen, Void>, TableCell<Citizen, Void>> {
    @Override
    public TableCell<Citizen, Void> call(final TableColumn<Citizen, Void> param) {
        return new TableCell<>() {
            private final Button btn = new Button("👁️");
            {
                btn.setOnAction((ActionEvent event) -> {
                    Citizen nk = getTableView().getItems().get(getIndex());
                    // mở cửa sổ chi tiết ở đây
                });
            }

            @Override
            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        };
    }
}