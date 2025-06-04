package controller;

import model.Citizen;
import org.hibernate.Transaction;
import service.CitizenDAO;
import service.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class CitizenController {

    public List<Citizen> getAllCitizens() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findAll();
        }
    }

    public void addCitizen(Citizen citizen) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            dao.save(citizen);
        }
    }

    public void updateCitizen(Citizen updatedCitizen) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            dao.update(updatedCitizen);
        }
    }

    public void deleteCitizen(Citizen citizen) throws Exception {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            int citizenId = citizen.getId();

            String relationship = (String) session.createNativeQuery("""
            SELECT relationshiptoowner FROM residence 
            WHERE citizenid = :cid
        """)
                    .setParameter("cid", citizenId)
                    .getSingleResult();

            if ("Head".equalsIgnoreCase(relationship)) {
                throw new Exception("Không thể xoá công dân vì là chủ hộ.");
            }

            session.createNativeQuery("DELETE FROM residence WHERE citizenid = :cid")
                    .setParameter("cid", citizenId)
                    .executeUpdate();

            session.createNativeQuery("DELETE FROM citizen WHERE citizenid = :cid")
                    .setParameter("cid", citizenId)
                    .executeUpdate();

            tx.commit();
        }
    }

    public List<Citizen> searchCitizenByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findByName(name);
        }
    }

    public List<Citizen> getCitizensWithPagination(int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.getWithPagination(page, pageSize);
        }
    }

    public Citizen getCitizenById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findById(id);
        }
    }

}
