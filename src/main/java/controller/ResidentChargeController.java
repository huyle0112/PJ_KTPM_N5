package controller;

import model.ResidentCharge;
import org.hibernate.Session;
import service.ResidentChargeDAO;

import java.util.List;

public class ResidentChargeController {
    private final Session session;
    private final ResidentChargeDAO residentChargeDAO;
    public ResidentChargeController(Session session) {
        this.session = session;
        this.residentChargeDAO = new ResidentChargeDAO(session);
    }

    public List<ResidentCharge> requestList(){
        return residentChargeDAO.getList();
    }

    public List<ResidentCharge> filter(String selectedType, String selectedCompletion){
        return residentChargeDAO.getList(selectedType, selectedCompletion);
    }

    public void create(ResidentCharge residentCharge){
        residentChargeDAO.save(residentCharge);
    }

    public void update(ResidentCharge residentCharge){
        residentChargeDAO.update(residentCharge);
    }

    public void delete(ResidentCharge residentCharge){
        residentChargeDAO.delete(residentCharge);

    }

    public List<ResidentCharge> search(String findKey, String selectedType, String selectedCompletion){
        return residentChargeDAO.search(findKey, selectedType, selectedCompletion);
    }
}
