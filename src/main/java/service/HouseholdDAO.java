package service;

import model.Citizen;
import model.Household;
import org.hibernate.Session;

import java.util.List;

public class HouseholdDAO extends GenericDAO<Household> {
    public HouseholdDAO(Session session) {
        super(Household.class, session);
    }

    public List<Household> findByOwnerName(String name) {
        String hql = "FROM Household h WHERE h.head.fullname ILIKE :name";
        return session.createQuery(hql, Household.class)
                    .setParameter("name", "%"+name+"%")
                    .getResultList();
    }

}
