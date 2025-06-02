//package view;
//
//import javafx.application.Application;
//import javafx.collections.*;
//import javafx.fxml.FXMLLoader;
//import javafx.geometry.*;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.*;
//import javafx.stage.Stage;
//import model.Citizen;
//import model.PaidRoom;
//import org.hibernate.Session;
//import service.GenericDAO;
//import service.HibernateUtil;
//
//import java.io.IOException;
//import java.util.Objects;
//
//public class main1 {
//
//    public static void main(String[] args) {
//        Session session = HibernateUtil.getSessionFactory().openSession();
//        GenericDAO<PaidRoom> dao = new GenericDAO<>(PaidRoom.class, session);
//        PaidRoom history = dao.findById(1);
//        history.setFullname("Nguyen Duc Manh");
//        dao.save(history);
//        PaidRoom history2 = dao.findById(1);
//        System.out.println(history2.getFullname());
//        //launch(args);
//    }
//
//
//    public void start(Stage primaryStage) throws IOException {
//        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass()
//                .getResource("/hi.fxml")));
//        primaryStage.setTitle("My Application");
//        primaryStage.setScene(new Scene(root));
//        primaryStage.show();
//    }
//
//
//}
