package application;

import controller.CitizenController;
import controller.ResidentChargeController;
import model.Citizen;
import model.ResidentCharge;
import org.hibernate.Session;
import org.hibernate.Transaction;
import service.CitizenDAO;
import service.GenericDAO;
import service.HibernateUtil;

import java.util.UUID;

public class CLIMain {
    public static void main(String[] args) {
        ResidentChargeController controller = new ResidentChargeController();
        ResidentCharge charge = new ResidentCharge();
        charge.setId(UUID.randomUUID());
        charge.setName("tháng 4 - vệ sinh");
        controller.create(charge);
    }
}
