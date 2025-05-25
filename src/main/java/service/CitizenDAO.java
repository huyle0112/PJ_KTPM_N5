package service;

import model.Citizen;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
}
