package controller;

import jakarta.persistence.NoResultException;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import model.Citizen;
import org.hibernate.Transaction;
import service.CitizenDAO;
import service.HibernateUtil;
import org.hibernate.Session;
import model.Citizen.ResidencyStatus;

import java.util.List;

public class CitizenController {

    public List<Citizen> getAllCitizens() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findAll();
        }
    }

    public void saveCitizen(Citizen citizen) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
             dao.save(citizen);
        }
    }

    public boolean addCitizen(Citizen citizen, Integer householdId, String relation, boolean isHead, ResidencyStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            CitizenDAO dao = new CitizenDAO(session);
            dao.save(citizen);

            session.flush();

            if (status == ResidencyStatus.Away || status == ResidencyStatus.Permanent) {
                if (householdId == null) {
                    tx.rollback();
                    Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Vui lòng nhập ID hộ khẩu!").showAndWait());
                    return false;
                }
                String _relation = isHead ? "Head" : relation;
                if (isHead) {
                    Object exists = session.createNativeQuery("""
                    SELECT 1 FROM residence WHERE householdid = :hid AND LOWER(relationshiptoowner) = 'head'
                """).setParameter("hid", householdId).uniqueResult();
                    if (exists != null) {
                        tx.rollback();
                        Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, "Hộ này đã có chủ hộ. Vui lòng kiểm tra lại!").showAndWait());
                        return false;
                    }
                }

                session.createNativeQuery("""
                INSERT INTO residence (citizenid, householdid, relationshiptoowner)
                VALUES (:cid, :hid, :rel)
            """)
                        .setParameter("cid", citizen.getId())
                        .setParameter("hid", householdId)
                        .setParameter("rel", _relation)
                        .executeUpdate();
            }

            tx.commit();
            return true;
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

            try{
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
            } catch (NoResultException e) {
                session.createNativeQuery("DELETE FROM citizen WHERE citizenid = :cid")
                        .setParameter("cid", citizenId)
                        .executeUpdate();
            }

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

    public List<Citizen> getMembersByHouseholdId(int householdId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findCitizensOfHousehold(householdId);
        }
    }
}
