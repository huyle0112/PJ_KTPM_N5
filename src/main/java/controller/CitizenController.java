package controller;

import model.Citizen;
import service.CitizenDAO;
import service.HibernateUtil;
import org.hibernate.Session;
import view.CitizenManagement.StatisticsResult;

import java.util.List;

public class CitizenController {

    public List<Citizen> getAllCitizens() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findAll();
        }
    }

    public void updateCitizen(Citizen citizen, Integer householdId, String relation, boolean isHead, Citizen.ResidencyStatus status) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            dao.updateCitizen(citizen, householdId, relation, isHead, status);
        }
    }

    public boolean addCitizen(Citizen citizen, Integer householdId, String relation, boolean isHead, Citizen.ResidencyStatus status){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.addCitizen(citizen, householdId, relation, isHead, status);
        }
    }

    public void deleteCitizen(Citizen citizen) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            dao.delete(citizen);
        }
    }

/*    public List<Citizen> searchCitizenByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findByName(name);
        }
    }*/

/*    public List<Citizen> getCitizensWithPagination(int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.getWithPagination(page, pageSize);
        }
    }*/

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

    public List<Citizen> getCitizensNotInHousehold() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findCitizensNotInHousehold();
        }
    }

    public StatisticsResult getCitizenStatistics() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.getCitizenStatistics();
        }
    }
}
