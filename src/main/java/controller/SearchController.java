
        package controller;

import model.Citizen;
import model.Household;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import service.CitizenDAO;
import service.HibernateUtil;
import service.HouseholdDAO;

import java.util.List;

public class SearchController {

    /**
     * Tìm kiếm nhân khẩu theo họ tên.
     * @param fullName Họ tên nhân khẩu (bắt buộc)
     * @return Danh sách nhân khẩu khớp với họ tên
     * @throws IllegalArgumentException Nếu không cung cấp họ tên
     * @throws RuntimeException Nếu có lỗi truy vấn CSDL
     */
    public List<Citizen> timNhanKhauTheoTen(String fullName) {
        // Kiểm tra đầu vào
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp họ tên để tìm kiếm nhân khẩu.");
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CitizenDAO dao = new CitizenDAO(session);
            return dao.findByName(fullName);
        } catch (Exception e) {
            throw new RuntimeException("Không thể truy vấn dữ liệu nhân khẩu: " + e.getMessage(), e);
        }
    }

    /**
     * Tìm kiếm hộ khẩu theo tên chủ hộ.
     * @param headName Tên chủ hộ (bắt buộc)
     * @return Danh sách hộ khẩu khớp với tên chủ hộ
     * @throws IllegalArgumentException Nếu không cung cấp tên chủ hộ
     * @throws RuntimeException Nếu có lỗi truy vấn CSDL
     */
    public List<Household> timHoKhauTheoTen(String headName) {
        // Kiểm tra đầu vào
        if (headName == null || headName.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng cung cấp tên chủ hộ để tìm kiếm hộ khẩu.");
        }

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            HouseholdDAO dao = new HouseholdDAO(session);
            List<Household> households = dao.findByOwnerName(headName);

            // Tải dữ liệu Citizen (chủ hộ) trong khi Session còn mở
            for (Household household : households) {
                Hibernate.initialize(household.getHead()); // Ép Hibernate tải dữ liệu Citizen
            }

            return households;
        } catch (Exception e) {
            throw new RuntimeException("Không thể truy vấn dữ liệu hộ khẩu: " + e.getMessage(), e);
        }
    }
}
