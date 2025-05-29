package view;

import application.SceneManager;
import controller.ResidentChargeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.ResidentCharge;
import org.hibernate.Session;
import service.HibernateUtil;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResidentChargeViewController implements Initializable, BlueMoonViewController {

    @FXML
    private Button addPresidentChargeButton;

    @FXML
    private Button accountantLogOutButton;

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
    private MenuButton inCompleteFilterButton;

    @FXML
    private MenuItem allTypeItem;

    @FXML
    private MenuItem voluntaryItem;

    @FXML
    private MenuItem mandatoryItem;

    @FXML
    private MenuItem allItem;

    @FXML
    private MenuItem inCompleteItem;

    @FXML
    private MenuItem completeItem;

    private ResidentChargeController residentChargeController;

    private SceneManager sceneManager;

    private String selectedType = "all";
    private String selectedCompletion = "all";
    private Session session = HibernateUtil.getSessionFactory().openSession();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        residentChargeController = new ResidentChargeController(session);
        sceneManager = new SceneManager();
        viewListCharge();
        addPresidentChargeButton.setOnAction(this::handleAddCharge);
        accountantLogOutButton.setOnAction(this::handleLogout);
        searchChargeButton.setOnAction(this::handleSearchCharge);
        chargeButton.setOnAction(this::handleCharge);
        allTypeItem.setOnAction(e -> {
            selectedType = "all";
            voluntaryFilterButton.setText("Tất cả");
            applyFilters();
        });

        voluntaryItem.setOnAction(e -> {
            selectedType = "voluntary";
            voluntaryFilterButton.setText("Tự nguyện");
            applyFilters();
        });

        mandatoryItem.setOnAction(e -> {
            selectedType = "mandatory";
            voluntaryFilterButton.setText("Bắt buộc");
            applyFilters();
        });

        // Xử lý bộ lọc trạng thái hoàn thành
        allItem.setOnAction(e -> {
            selectedCompletion = "all";
            inCompleteFilterButton.setText("Tất cả");
            applyFilters();
        });

        inCompleteItem.setOnAction(e -> {
            selectedCompletion = "false";
            inCompleteFilterButton.setText("Chưa hoàn thành");
            applyFilters();
        });

        completeItem.setOnAction(e -> {
            selectedCompletion = "true";
            inCompleteFilterButton.setText("Đã hoàn thành");
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
        if(!    findKey.isEmpty())listCharge = residentChargeController.search(findKey,selectedType,selectedCompletion);
        for (ResidentCharge charge : listCharge) {
            VBox card = createChargeCard(charge);
            listOfPresidentChargeBox.getChildren().add(card);
        }
    }

    private void handleAddCharge(ActionEvent evt) {
        exitScene();
        sceneManager.showViewWithOutController("/AddChargeView.fxml", "Thêm khoản thu");
    }

    private void handleLogout(ActionEvent evt) {
        exitScene();
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
        exitScene();
        sceneManager.showViewWithOutController("/ChargeView.fxml","Thu phí");
    }

    private void exitScene(){
        session.close();
        Stage stage = (Stage) accountantLogOutButton.getScene().getWindow();
        stage.close();
    }
}
