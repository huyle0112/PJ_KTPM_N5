package service;

import model.PaidHousehold;
import org.hibernate.Session;

public class PaidHouseholdDAO extends GenericDAO<PaidHousehold>{
    public PaidHouseholdDAO(Session session){
        super(PaidHousehold.class, session);
    }
}
