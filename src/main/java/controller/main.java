
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

        // Gọi các hàm kiểm thử mới cho SearchController
        testSearchCitizenByName();
        testSearchHouseholdByName();
    }

    // Hàm kiểm thử tìm kiếm nhân khẩu theo tên với nhiều trường hợp
    private static void testSearchCitizenByName() {
        SearchController searchController = new SearchController();
        System.out.println("\n=== Kiểm thử tìm kiếm nhân khẩu theo tên ===");

        // Trường hợp 1: Tìm kiếm với tên "Nguyen"
        System.out.println("Tìm kiếm nhân khẩu với tên 'Nguyen':");
        try {
            List<Citizen> citizens = searchController.timNhanKhauTheoTen("Nguyen");
            if (citizens.isEmpty()) {
                System.out.println("Không tìm thấy nhân khẩu nào với tên 'Nguyen'.");
            } else {
                for (Citizen citizen : citizens) {
                    System.out.println("Nhân khẩu: " + citizen.getFullname() + ", ID: " + citizen.getId());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi tìm nhân khẩu với tên 'Nguyen': " + e.getMessage());
        }

        // Trường hợp 2: Tìm kiếm với tên "Le"
        System.out.println("\nTìm kiếm nhân khẩu với tên 'Le':");
        try {
            List<Citizen> citizens = searchController.timNhanKhauTheoTen("Le");
            if (citizens.isEmpty()) {
                System.out.println("Không tìm thấy nhân khẩu nào với tên 'Le'.");
            } else {
                for (Citizen citizen : citizens) {
                    System.out.println("Nhân khẩu: " + citizen.getFullname() + ", ID: " + citizen.getId());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi tìm nhân khẩu với tên 'Le': " + e.getMessage());
        }

        // Trường hợp 3: Tìm kiếm với tên không tồn tại (giả lập kiểm tra không tìm thấy)
        System.out.println("\nTìm kiếm nhân khẩu với tên 'NonExistentName':");
        try {
            List<Citizen> citizens = searchController.timNhanKhauTheoTen("NonExistentName");
            if (citizens.isEmpty()) {
                System.out.println("Không tìm thấy nhân khẩu nào với tên 'NonExistentName'.");
            } else {
                for (Citizen citizen : citizens) {
                    System.out.println("Nhân khẩu: " + citizen.getFullname() + ", ID: " + citizen.getId());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi tìm nhân khẩu với tên 'NonExistentName': " + e.getMessage());
        }
    }

    // Hàm kiểm thử tìm kiếm hộ khẩu theo tên chủ hộ với nhiều trường hợp
    private static void testSearchHouseholdByName() {
        SearchController searchController = new SearchController();
        System.out.println("\n=== Kiểm thử tìm kiếm hộ khẩu theo tên chủ hộ ===");

        // Trường hợp 1: Tìm kiếm với tên chủ hộ "Le"
        System.out.println("Tìm kiếm hộ khẩu với tên chủ hộ 'Le':");
        try {
            List<Household> households = searchController.timHoKhauTheoTen("Le");
            if (households.isEmpty()) {
                System.out.println("Không tìm thấy hộ khẩu nào với tên chủ hộ 'Le'.");
            } else {
                for (Household household : households) {
                    System.out.println("Hộ khẩu: " + household.getHead().getFullname() + ", ID: " + household.getId());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi tìm hộ khẩu với tên chủ hộ 'Le': " + e.getMessage());
        }

        // Trường hợp 2: Tìm kiếm với tên chủ hộ "Nguyen"
        System.out.println("\nTìm kiếm hộ khẩu với tên chủ hộ 'Nguyen':");
        try {
            List<Household> households = searchController.timHoKhauTheoTen("Nguyen");
            if (households.isEmpty()) {
                System.out.println("Không tìm thấy hộ khẩu nào với tên chủ hộ 'Nguyen'.");
            } else {
                for (Household household : households) {
                    System.out.println("Hộ khẩu: " + household.getHead().getFullname() + ", ID: " + household.getId());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi tìm hộ khẩu với tên chủ hộ 'Nguyen': " + e.getMessage());
        }

        // Trường hợp 3: Tìm kiếm với tên chủ hộ không tồn tại (giả lập kiểm tra không tìm thấy)
        System.out.println("\nTìm kiếm hộ khẩu với tên chủ hộ 'NonExistentName':");
        try {
            List<Household> households = searchController.timHoKhauTheoTen("NonExistentName");
            if (households.isEmpty()) {
                System.out.println("Không tìm thấy hộ khẩu nào với tên chủ hộ 'NonExistentName'.");
            } else {
                for (Household household : households) {
                    System.out.println("Hộ khẩu: " + household.getHead().getFullname() + ", ID: " + household.getId());
                }
            }
        } catch (Exception e) {
            System.err.println("Lỗi tìm hộ khẩu với tên chủ hộ 'NonExistentName': " + e.getMessage());
        }
    }
}
