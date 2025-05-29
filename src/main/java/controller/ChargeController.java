package controller;

import model.PaidHousehold;
import org.hibernate.Session;
import service.PaidHouseholdDAO;

public class ChargeController {
    private final Session session;
    private final PaidHouseholdDAO paidHouseholdDAO;

    public ChargeController(Session session){
        this.session = session;
        this.paidHouseholdDAO = new PaidHouseholdDAO(session);

    }

    public void charge(PaidHousehold paidHousehold){
        paidHouseholdDAO.save(paidHousehold);
    }
}
