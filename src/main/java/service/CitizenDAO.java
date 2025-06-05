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
        String hql = "SELECT c, r.relationshiptoowner FROM Residence r JOIN r.citizenid c WHERE r.householdid = :hid";
        List<Object[]> results = session.createQuery(hql, Object[].class)
                .setParameter("hid", household)
                .getResultList();
        List<Citizen> citizens = new java.util.ArrayList<>();
        for (Object[] row : results) {
            Citizen citizen = (Citizen) row[0];
            String relationship = (String) row[1];
            citizen.setRelationshipToOwner(relationship);
            citizens.add(citizen);
        }
        return citizens;
    }
}
