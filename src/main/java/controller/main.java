package controller;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import model.Citizen;
import model.Household;
import service.CitizenDAO;
import service.HibernateUtil;
import service.HouseholdDAO;

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
           
           householdCheck();
       }
    }
    
    public static void householdCheck() {

        assert HibernateUtil.getSessionFactory() != null;

        SessionFactory sessionFactory = null;
        sessionFactory = HibernateUtil.getSessionFactory();

        System.out.println("Setting up initial citizen data...");


        // Save citizens using a temporary session and CitizenDAO
        Session tempSession = sessionFactory.openSession();
        
        CitizenDAO citizenDAO = new CitizenDAO(tempSession);
        List<Citizen> citizens = citizenDAO.findAll();

        Citizen citizen1 = citizens.get(0);
        Citizen citizen2 = citizens.get(1);
        Citizen citizen3 = citizens.get(2);
        Citizen citizen4 = citizens.get(3);

        System.out.println("Citizen 1 ID: " + citizen1.getId());
        System.out.println("Citizen 2 ID: " + citizen2.getId());
        System.out.println("Citizen 3 ID: " + citizen3.getId());
        System.out.println("Citizen 4 ID: " + citizen4.getId());
        
        HouseholdDAO householdDAO = new HouseholdDAO(tempSession);
        List<Household> households = householdDAO.findAll();

        Household hh1 = households.get(0);
        Household hh2 = households.get(1);

        System.out.println("Household 1 ID: " + hh1.getId());
        System.out.println("Household 2 ID: " + hh2.getId());
        
        
        System.out.println("Initial citizen data setup complete.\n");
        
        HouseholdController householdController = new HouseholdController(tempSession);
        
        // 1. Thêm hộ khẩu
        /*
        System.out.println("--- Testing Add Household ---");
        Household hh1 = new Household();
        hh1.setAddress("123 Duong So 1, Quan 1, TP HCM");
        hh1.setHead(citizen1); // citizen1 is now managed and has an ID
        hh1.setId(101);

        if (householdController.addHousehold(hh1)) {
            System.out.println("Household 1 added successfully with ID: " + hh1.getId());
        } else {
            System.out.println("Failed to add household 1.");
        }

        Household hh2 = new Household();
        hh2.setAddress("456 Duong So 2, Quan 2, TP HCM");
        hh2.setHead(citizen2);
        hh2.setId(102);
        if (householdController.addHousehold(hh2)) {
            System.out.println("Household 2 added successfully with ID: " + hh2.getId());
        } else {
            System.out.println("Failed to add household 2.");
        }
        System.out.println();
        
        Citizen managedCitizen4 = citizenDAO.findById(citizen4.getId());
        Household managedHh1 = new HouseholdDAO(tempSession).findById(hh1.getId());
        if (managedCitizen4 != null && managedHh1 != null) {
     	   citizenDAO.setHousehold(managedCitizen4, managedHh1, "Con"); // Assuming Citizen has setHousehold()
            citizenDAO.update(managedCitizen4);

            System.out.println("Citizen 4 associated with Household 1.");
        }
        */

        // 2. Lấy tất cả
        System.out.println("--- Testing Get All Households ---");
        List<Household> allHouseholds = householdController.getAllHouseholds();
        if (allHouseholds.isEmpty()) {
            System.out.println("No households found.");
        } else {
            allHouseholds.forEach(hhs -> System.out.println(
                "ID: " + hhs.getId() + ", Address: " + hhs.getAddress() + 
                ", Head: " + (hhs.getHead() != null ? hhs.getHead().getFullname() : "N/A") +
                " (Head ID: " + (hhs.getHead() != null ? hhs.getHead().getId() : "N/A") + ")"
            ));
        }
        System.out.println();

        // 3. Duyệt thông tin hộ khẩu theo ID
        System.out.println("--- Testing Get Household By ID (" + hh1.getId() + ") ---");
        Household foundHh = householdController.getHouseholdById(hh1.getId());
        if (foundHh != null) {
            System.out.println("Found Household: ID " + foundHh.getId() + ", Address: " + foundHh.getAddress() +
                               ", Head: " + foundHh.getHead().getFullname());
        } else {
            System.out.println("Household with ID " + hh1.getId() + " not found.");
        }
        System.out.println();

        // 4. Duyệt thông tin hộ khẩu theo tên chủ hộ
        System.out.println("--- Testing Find Households by Owner Name (Nguyen Van A) ---");
        List<Household> householdsByOwner = householdController.findHouseholdsByOwnerName("Nguyen Van A");
        householdsByOwner.forEach(hhs -> System.out.println("Found: ID " + hhs.getId() + ", Address: " + hhs.getAddress() +
                                                        ", Head: " + hhs.getHead().getFullname()));
        System.out.println();

        // 5. Thay đổi chủ hộ
        System.out.println("--- Testing Change Household Head (Household ID: " + hh1.getId() + ", New Head ID: " + citizen3.getId() + ") ---");
        if (householdController.changeHouseholdHead(hh1.getId(), citizen3.getId())) {
            System.out.println("Successfully changed head of household " + hh1.getId() + " to " + citizen3.getFullname());
            Household updatedHh1 = householdController.getHouseholdById(hh1.getId());
            System.out.println("New Head of HH1: " + updatedHh1.getHead().getFullname());
        } else {
            System.out.println("Failed to change household head.");
        }
        System.out.println();

        // 6. Cập nhật thông tin hộ khẩu
        System.out.println("--- Testing Update Household (ID: "+ hh2.getId() +") ---");
        Household toUpdateHh2 = householdController.getHouseholdById(hh2.getId());
        if (toUpdateHh2 != null) {
            toUpdateHh2.setAddress("789 Duong So 3, Quan Binh Thanh, TP HCM (Updated)");
            if (householdController.updateHousehold(toUpdateHh2)) {
                System.out.println("Household " + hh2.getId() + " updated successfully.");
                Household updatedHh2 = householdController.getHouseholdById(hh2.getId());
                System.out.println("Updated Address: " + updatedHh2.getAddress());
            } else {
                 System.out.println("Failed to update household " + hh2.getId());
            }
        } else {
             System.out.println("Household " + hh2.getId() + " not found for update.");
        }
        System.out.println();

        // 7. Duyệt thông tin nhân khẩu theo phòng
        System.out.println("--- Testing Get Members By Household ID (" + hh1.getId() + ") ---");
        List<Citizen> members = householdController.getMembersByHouseholdId(hh1.getId());
        if (members == null || members.isEmpty()) {
            System.out.println("No members found for household ID " + hh1.getId() + " (or household doesn't exist).");
        } else {
            System.out.println("Members of household ID " + hh1.getId() + ":");
            members.forEach(c -> System.out.println(" - " + c.getFullname() + " (ID: " + c.getId() + ")"));
        }
        System.out.println();


        // 8. Xóa hộ khẩu
        /*
        System.out.println("--- Testing Delete Household (ID: " + hh2.getId() + ") ---");
        if (householdController.deleteHousehold(hh2.getId())) {
            System.out.println("Household " + hh2.getId() + " deleted successfully.");
        } else {
            System.out.println("Failed to delete household " + hh2.getId() + ".");
        }
        // Verify deletion
        Household deletedHh2 = householdController.getHouseholdById(hh2.getId());
        if (deletedHh2 == null) {
            System.out.println("Household " + hh2.getId() + " confirmed deleted.");
        } else {
            System.out.println("Household " + hh2.getId() + " still exists after deletion attempt.");
        }
        System.out.println();
        */
    }
}
