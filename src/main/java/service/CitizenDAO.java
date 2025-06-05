package service;

import model.Citizen;
import model.Household;
import org.hibernate.Session;

import java.util.List;

public class CitizenDAO extends GenericDAO<Citizen> {
    public CitizenDAO(Session session) {
        super(Citizen.class, session);
    }

    public List<Citizen> findByName(String name) {
        String hql = "FROM Citizen c WHERE lower(c.fullname) LIKE lower(:name)";
        return session.createQuery(hql, Citizen.class)
                .setParameter("name", "%" + name + "%")
                .getResultList();
    }

    public List<Citizen> findCitizensOfHousehold(int householdId) {
        Household household = session.get(Household.class, householdId);
        String hql = "SELECT c FROM Residence r JOIN r.citizenid c WHERE r.householdid = :hid";
        return session.createQuery(hql, Citizen.class)
                .setParameter("hid", household)
                .getResultList();
    }
}
