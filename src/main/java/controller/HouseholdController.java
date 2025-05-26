
        package controller;

import model.Household;
import org.hibernate.Session;
import service.HouseholdDAO;
import java.util.List;

public class HouseholdController {
    private final HouseholdDAO householdDAO;

    public HouseholdController(Session session) {
        this.householdDAO = new HouseholdDAO(session);
    }

    /**
     * Tìm kiếm hộ khẩu dựa trên tên chủ hộ hoặc mã hộ khẩu.
     * @param headName Tên chủ hộ (có thể null nếu không tìm theo tiêu chí này)
     * @param householdId Mã hộ khẩu (có thể null)
     * @return Danh sách hộ khẩu khớp với tiêu chí
     * @throws IllegalArgumentException Nếu không cung cấp ít nhất một tiêu chí
     * @throws RuntimeException Nếu có lỗi truy vấn CSDL
     */
    public List<Household> timHoKhau(String headName, Integer householdId) {
        // Kiểm tra đầu vào: cần ít nhất một tiêu chí
        if ((headName == null || headName.trim().isEmpty()) &&
                householdId == null) {
            throw new IllegalArgumentException("Vui lòng cung cấp ít nhất một tiêu chí tìm kiếm.");
        }

        try {
            // Tìm kiếm hộ khẩu
            List<Household> result;
            if (headName != null && !headName.trim().isEmpty()) {
                // Tìm theo tên chủ hộ
                result = householdDAO.findByOwnerName(headName);
            } else {
                // Tìm theo mã hộ khẩu
                result = householdDAO.findAll();
            }

            // Lọc thủ công theo mã hộ khẩu nếu có
            if (householdId != null) {
                result.removeIf(household -> !household.getId().equals(householdId));
            }

            if (result.isEmpty()) {
                System.out.println("Không tìm thấy hộ khẩu phù hợp.");
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Không thể truy vấn dữ liệu: " + e.getMessage(), e);
        }
    }
}