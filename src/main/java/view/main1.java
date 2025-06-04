package view;

import controller.CitizenController;
import model.Citizen;
import org.hibernate.SessionFactory;
import service.*;

public class main1 {

    public static void main(String[] args) {
        CitizenController citizenController = new CitizenController();
        CitizenDAO citizenDAO = new CitizenDAO(HibernateUtil.getSessionFactory().openSession());
        Citizen citizen2 = new Citizen();
        citizen2.setFullname("Nguyen Van Manh");
        citizen2.setResidencyStatus(Citizen.ResidencyStatus.valueOf("Away"));

        citizenDAO.save(citizen2);
    }




}
