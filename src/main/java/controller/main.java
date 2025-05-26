
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

        // Gọi các hàm kiểm thử cho CitizenController và HouseholdController
        testCitizenController();
        testHouseholdController();
    }

    // Hàm kiểm thử cho CitizenController
    private static void testCitizenController() {
        System.out.println("\n=== Kiểm thử CitizenController ===");
        // Mở một Session mới cho CitizenController
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenController citizenController = new CitizenController(session);
            try {
                List<Citizen> citizens = citizenController.timNhanKhau("Nguyen", null, null);
                if (citizens.isEmpty()) {
                    System.out.println("Không tìm thấy nhân khẩu nào.");
                } else {
                    for (Citizen citizen : citizens) {
                        System.out.println("Nhân khẩu: " + citizen.getFullname() + ", ID: " + citizen.getId());
                    }
                }
            } catch (Exception e) {
                System.err.println("Lỗi tìm nhân khẩu: " + e.getMessage());
            }
        }
    }

    // Hàm kiểm thử cho HouseholdController
    private static void testHouseholdController() {
        System.out.println("\n=== Kiểm thử HouseholdController ===");
        // Mở một Session mới cho HouseholdController
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            HouseholdController householdController = new HouseholdController(session);
            try {
                List<Household> households = householdController.timHoKhau("Le", null);
                if (households.isEmpty()) {
                    System.out.println("Không tìm thấy hộ khẩu nào.");
                } else {
                    for (Household household : households) {
                        System.out.println("Hộ khẩu: " + household.getHead().getFullname() + ", ID: " + household.getId());
                    }
                }
            } catch (Exception e) {
                System.err.println("Lỗi tìm hộ khẩu: " + e.getMessage());
            }
        }
    }
}
