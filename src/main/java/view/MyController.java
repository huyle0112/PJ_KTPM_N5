package view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import model.Citizen;
import service.CitizenDAO;

public class MyController implements Initializable {
    @FXML
    private TableView<Citizen> citizenTable;
    @FXML private TableColumn<Citizen, Integer> idColumn;
    @FXML private TableColumn<Citizen, String> nameColumn;
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        nameColumn.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getFullname()));

        Citizen citizen = new Citizen();
        citizen.setId(5);
        citizen.setFullname("Nguyen Duc Manh");
        Citizen citizen2 = new Citizen();
        citizen2.setId(2);
        citizen2.setFullname("Nguyen Duc Dung");

        ObservableList<Citizen> citizenList = FXCollections.observableArrayList();
        citizenTable.setItems(citizenList);
        citizenList.addAll(citizen, citizen2);

    }

}