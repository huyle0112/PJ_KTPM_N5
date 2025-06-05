package controller;

import model.ResidentCharge;
import org.hibernate.Session;
import service.CitizenDAO;
import service.HibernateUtil;
import service.ResidentChargeDAO;

import java.util.List;
import java.util.UUID;

public class ResidentChargeController {

    public ResidentChargeController() {
    }

    public List<ResidentCharge> requestList(){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ResidentChargeDAO residentChargeDAO = new ResidentChargeDAO(session);
            return residentChargeDAO.getList();
        }

    }

    public List<ResidentCharge> filter(String selectedType, String selectedCompletion){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ResidentChargeDAO residentChargeDAO = new ResidentChargeDAO(session);
            return residentChargeDAO.getList(selectedType, selectedCompletion);
        }
    }

    public void create(ResidentCharge residentCharge){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ResidentChargeDAO dao = new ResidentChargeDAO(session);
            dao.save(residentCharge);
        }
    }

    public void update(ResidentCharge residentCharge){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ResidentChargeDAO residentChargeDAO = new ResidentChargeDAO(session);
            residentChargeDAO.update(residentCharge);
        }
    }

    public void delete(ResidentCharge residentCharge){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ResidentChargeDAO residentChargeDAO = new ResidentChargeDAO(session);
            residentChargeDAO.delete(residentCharge);
        }
    }

    public List<ResidentCharge> search(String findKey, String selectedType, String selectedCompletion){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ResidentChargeDAO residentChargeDAO = new ResidentChargeDAO(session);
            return residentChargeDAO.search(findKey, selectedType, selectedCompletion);
        }
    }

    public ResidentCharge findById(UUID id){
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            ResidentChargeDAO residentChargeDAO = new ResidentChargeDAO(session);
            return residentChargeDAO.findById(id);
        }
    }
}
