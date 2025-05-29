package service;

import model.ResidentCharge;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class ResidentChargeDAO extends GenericDAO<ResidentCharge>{
    public ResidentChargeDAO(Session session){
        super(ResidentCharge.class, session);
    }

    public List<ResidentCharge> getList() {
        String hql = "SELECT DISTINCT c FROM ResidentCharge c LEFT JOIN FETCH c.paidHouseholdList";
        return session.createQuery(hql, ResidentCharge.class).getResultList();
    }

    public List<ResidentCharge> getList(String selectedType, String selectedCompletion){
        List<ResidentCharge> residentCharges = getList();
        List<ResidentCharge> list = new ArrayList<>();
        for(ResidentCharge charge : residentCharges){
            if((charge.getTypeOfCharge().equals(selectedType) || selectedType.equals("all")) &&
                    (charge.getInComplete().equals(selectedCompletion.equals("false")) || selectedCompletion.equals("all"))){
                list.add(charge);
            }
        }
        return list;
    }
    public List<ResidentCharge> search(String findKey, String selectedType, String selectedCompletion){
        List<ResidentCharge> residentCharges = findAll();
        List<ResidentCharge> list = new ArrayList<>();
        for(ResidentCharge charge : residentCharges){
            if(charge.getName().toLowerCase().contains(findKey.toLowerCase()) ||
                    charge.getDescription().toLowerCase().contains(findKey.toLowerCase())){
                if((charge.getTypeOfCharge().equals(selectedType) || selectedType.equals("all")) &&
                        (charge.getInComplete().equals(selectedCompletion.equals("false")) || selectedCompletion.equals("all"))){
                    list.add(charge);
                }
            }
        }
        return list;
    }
}
