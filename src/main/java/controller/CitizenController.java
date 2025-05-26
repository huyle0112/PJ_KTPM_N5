package controller;

import model.Citizen;
import org.hibernate.Session;
import service.CitizenDAO;
import service.HibernateUtil;

import java.util.List;
public class CitizenController {
    public void addCitizen(Citizen citizen) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            dao.save(citizen);
        }
    }

    public void updateCitizen(Citizen citizen) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            dao.update(citizen);
        }
    }

    public void deleteCitizen(Citizen citizen) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            dao.delete(citizen);
        }
    }

    public Citizen getCitizenById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findById(id);
        }
    }

    public List<Citizen> getAllCitizens() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findAll();
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
}
