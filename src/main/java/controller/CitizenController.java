package controller;

import model.Citizen;
import service.CitizenDAO;
import service.HibernateUtil;
import org.hibernate.Session;

import java.util.List;
public class CitizenController {

    public List<Citizen> getCitizensOfHousehold(int householdId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findCitizensOfHousehold(householdId);
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

    public void deleteCitizen(Citizen citizen) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            dao.delete(citizen);
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
