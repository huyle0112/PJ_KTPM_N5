package controller;

import model.*;
import org.hibernate.Session;
import service.CitizenDAO;
import service.GenericDAO;
import service.HibernateUtil;
import service.HouseholdDAO;

import java.util.List;

public class main {
    public static void main(String[] args) {
        assert HibernateUtil.getSessionFactory() != null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
           HouseholdDAO dao = new HouseholdDAO(session);
           List<Household> households = dao.findAll();
           for (Household household : households) {
               System.out.println(household.getHead().getFullname());
           }
           List<Household> h = dao.findByOwnerName("Nguyen");
           for (Household hh : h) {
               System.out.println(hh.getHead().getFullname());
           }
       }
    }
}
