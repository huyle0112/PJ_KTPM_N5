package view;

import application.SceneManager;
import controller.ResidentChargeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ResidentCharge;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResidentChargeViewController implements Initializable, BlueMoonViewController {

    @FXML
    private Button addPresidentChargeButton;

    @FXML
    private Button homeButton;

    @FXML
    private VBox listOfPresidentChargeBox;

    @FXML
    private Button searchChargeButton;

    @FXML
    private Button chargeButton;

    @FXML
    private TextField findKeyTextField;

    @FXML
    private MenuButton voluntaryFilterButton;

    @FXML
    private MenuItem allTypeItem;

    @FXML
    private MenuItem voluntaryItem;

    @FXML
    private MenuItem mandatoryItem;

    @FXML
    private MenuItem allItem;

    private ResidentChargeController residentChargeController;

    private SceneManager sceneManager;

    private String selectedType = "all";
    private String selectedCompletion = "all";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        residentChargeController = new ResidentChargeController();
        sceneManager = new SceneManager();
        viewListCharge();
        addPresidentChargeButton.setOnAction(this::handleAddCharge);

        searchChargeButton.setOnAction(this::handleSearchCharge);
        chargeButton.setOnAction(this::handleCharge);
        allTypeItem.setOnAction(e -> {
            selectedType = "all";
            selectedCompletion = "all";
            voluntaryFilterButton.setText("Tất cả");
            applyFilters();
        });

        voluntaryItem.setOnAction(e -> {
            selectedType = "Voluntary";
            selectedCompletion = "all";
            voluntaryFilterButton.setText("Khoản thu tự nguyện");
            applyFilters();
        });

        mandatoryItem.setOnAction(e -> {
            selectedType = "Mandatory";
            selectedCompletion = "false";
            voluntaryFilterButton.setText("Khoản thu bắt buộc chưa hoàn thành");
            applyFilters();
        });

        // Xử lý bộ lọc trạng thái hoàn thành
        allItem.setOnAction(e -> {
            selectedType = "Mandatory";
            selectedCompletion = "true";
            voluntaryFilterButton.setText("Khoản thu bắt buộc đã hoàn thành");
            applyFilters();
        });
    }

    private void applyFilters() {
        System.out.println("Lọc theo:");
        System.out.println("Loại phí: " + selectedType);
        System.out.println("Trạng thái: " + selectedCompletion);
        viewListCharge(selectedType, selectedCompletion);
    }

    private void viewListCharge(){
        listOfPresidentChargeBox.getChildren().clear();
        List<ResidentCharge> listCharge = residentChargeController.requestList();
        for(ResidentCharge charge : listCharge){
            VBox card = createChargeCard(charge);
            listOfPresidentChargeBox.getChildren().add(card);
        }
    }

    private void viewListCharge(String selectedType, String selectedCompletion) {
        listOfPresidentChargeBox.getChildren().clear();

        List<ResidentCharge> listCharge = residentChargeController.filter(selectedType, selectedCompletion);
        String findKey = findKeyTextField.getText();
        if(!findKey.isEmpty())listCharge = residentChargeController.search(findKey,selectedType,selectedCompletion);
        for (ResidentCharge charge : listCharge) {
            VBox card = createChargeCard(charge);
            listOfPresidentChargeBox.getChildren().add(card);
        }
    }

    private void handleAddCharge(ActionEvent evt) {
        AddChargeViewController controller = new AddChargeViewController();
        controller.setParentController(this);
        sceneManager.showViewWithController("/AddChargeView.fxml", controller,"Thêm khoản thu");
    }

    @FXML
    private void returnHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
            Parent citizenView = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(citizenView);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private VBox createChargeCard(ResidentCharge residentCharge) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResidentChargeCard.fxml"));
            VBox card = loader.load();
            ResidentChargeCardController controller = loader.getController();
            controller.setData(residentCharge);
            card.setOnMouseClicked(event ->  showChargeDetails(event, residentCharge));
            return card;
        } catch (IOException e) {
            e.printStackTrace();
            return new VBox();
        }
    }

    private void handleSearchCharge(ActionEvent e){
        String findKey = findKeyTextField.getText();
        listOfPresidentChargeBox.getChildren().clear();
        List<ResidentCharge> listCharge = residentChargeController.search(findKey, selectedType, selectedCompletion);
        for(ResidentCharge charge : listCharge){
            VBox card = createChargeCard(charge);
            listOfPresidentChargeBox.getChildren().add(card);
        }
    }

    private void showChargeDetails(MouseEvent event, ResidentCharge residentCharge){
        exitScene();
        ChargeDetailsViewController chargeDetailsViewController = new ChargeDetailsViewController(residentCharge);
        sceneManager.showViewWithController("/ChargeDetailsView.fxml", chargeDetailsViewController, "Khoản thu");
    }

    private void handleCharge(ActionEvent event){
        ResidentChargeSelectedViewController controller = new ResidentChargeSelectedViewController();
        controller.setParentController(this);
        sceneManager.showViewWithController("/ResidentChargeSelectedView.fxml",controller,"Thu phí");
    }

    private void exitScene(){
        Stage stage = (Stage) homeButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void reload() {
        listOfPresidentChargeBox.getChildren().clear();
        viewListCharge();
    }

}
