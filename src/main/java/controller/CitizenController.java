
package controller;

import model.Citizen;
import org.hibernate.Session;
import service.CitizenDAO;
import java.time.LocalDate;
import java.util.List;

public class CitizenController {
    private final CitizenDAO citizenDAO;

    public CitizenController(Session session) {
        this.citizenDAO = new CitizenDAO(session);
    }

    /**
     * Tìm kiếm nhân khẩu dựa trên các tiêu chí: họ tên, mã nhân khẩu, ngày sinh.
     * @param fullName Họ tên nhân khẩu (có thể null nếu không tìm theo tiêu chí này)
     * @param citizenId Mã nhân khẩu (có thể null)
     * @param dateOfBirth Ngày sinh (có thể null)
     * @return Danh sách nhân khẩu khớp với tiêu chí
     * @throws IllegalArgumentException Nếu không cung cấp ít nhất một tiêu chí
     * @throws RuntimeException Nếu có lỗi truy vấn CSDL
     */
    public List<Citizen> timNhanKhau(String fullName, Integer citizenId, LocalDate dateOfBirth) {
        // Kiểm tra đầu vào: cần ít nhất một tiêu chí
        if ((fullName == null || fullName.trim().isEmpty()) &&
                citizenId == null &&
                dateOfBirth == null) {
            throw new IllegalArgumentException("Vui lòng cung cấp ít nhất một tiêu chí tìm kiếm.");
        }

        try {
            // Hiện tại CitizenDAO chỉ hỗ trợ tìm theo tên, cần mở rộng để tìm theo các tiêu chí khác
            // Tạm thời chỉ tìm theo tên và lọc thủ công các tiêu chí còn lại
            List<Citizen> result;
            if (fullName != null && !fullName.trim().isEmpty()) {
                result = citizenDAO.findByName(fullName);
            } else {
                result = citizenDAO.findAll();
            }

            // Lọc thủ công theo các tiêu chí khác
            result.removeIf(citizen -> {
                boolean matchId = citizenId == null || citizen.getId().equals(citizenId);
                boolean matchDob = dateOfBirth == null || (citizen.getDateofbirth() != null && citizen.getDateofbirth().equals(dateOfBirth));
                return !matchId || !matchDob;
            });

            if (result.isEmpty()) {
                System.out.println("Không tìm thấy nhân khẩu phù hợp.");
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Không thể truy vấn dữ liệu: " + e.getMessage(), e);
        }
    }
}
