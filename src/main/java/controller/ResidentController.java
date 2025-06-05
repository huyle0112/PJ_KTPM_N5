package controller;

import model.Residence;
import service.ResidentDAO;
import service.HibernateUtil;
import org.hibernate.Session;

public class ResidentController {
    public void addCitizenToHousehold(int citizenId, int householdId, String relation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ResidentDAO dao = new ResidentDAO(session);
            dao.addCitizenToHousehold(citizenId, householdId, relation);
        }
    }

    public void updateRelationship(int citizenId, int householdId, String newRelation) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ResidentDAO dao = new ResidentDAO(session);
            dao.updateRelationship(citizenId, householdId, newRelation);
        }
    }

    public void removeCitizenFromHousehold(int citizenId, int householdId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ResidentDAO dao = new ResidentDAO(session);
            dao.removeCitizenFromHousehold(citizenId, householdId);
        }
    }

    public void changeHouseholdHead(int newHeadCitizenId, int householdId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ResidentDAO dao = new ResidentDAO(session);
            dao.changeHouseholdHead(newHeadCitizenId, householdId);
        }
    }
} 