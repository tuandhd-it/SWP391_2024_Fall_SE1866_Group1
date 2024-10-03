package project.dental_clinic_management.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.dental_clinic_management.dto.request.ClinicBranchCreationRequest;
import project.dental_clinic_management.dto.request.ClinicBranchUpdateRequest;
import project.dental_clinic_management.dto.request.EmployeeChangePasswordRequest;
import project.dental_clinic_management.dto.request.EmployeeUpdateRequest;
import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Employee;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@SpringBootTest
public class AdminServiceTest {

    @Autowired
    private AdminService adminService;
    private LocalDate dob;


    @Test
    void getEmployeeTest() {
        List<Employee> list = adminService.getAllEmployees();

        dob = LocalDate.of(2004, 12, 9);

        assertEquals(1, list.get(0).getEmp_id());
        assertEquals("Đỗ", list.get(0).getFirst_name());
        assertEquals("Tuấn", list.get(0).getLast_name());
        assertEquals("tuan6a1thcstv@gmail.com", list.get(0).getEmail());
        assertEquals("0123456789", list.get(0).getPhone());
        assertEquals(dob, list.get(0).getDob());
        assertEquals("Nam", list.get(0).getGender());
        assertEquals("Ha Noi", list.get(0).getAddress());
        assertEquals(4000, list.get(0).getSalary());
    }

    @Test
    void getEmployeeByIdTest() {
        Employee employee = adminService.getEmployeeById(4);

        dob = LocalDate.of(2004, 3, 29);

        assertEquals(4, employee.getEmp_id());
        assertEquals("Nguyễn Việt", employee.getFirst_name());
        assertEquals("Lâm", employee.getLast_name());
        assertEquals("nguyenvietlam290304@gmail.com", employee.getEmail());
        assertEquals("0378790992", employee.getPhone());
        assertEquals(dob, employee.getDob());
        assertEquals("male", employee.getGender());
        assertEquals("Ha noi", employee.getAddress());
        assertEquals(0, employee.getSalary());
    }

    @Test
    void changePasswordTest() {
        EmployeeChangePasswordRequest request = new EmployeeChangePasswordRequest();
        request.setNewPassword("newPassword");
        request.setConfirmPassword("newPassword");

        Employee updatedEmployee = adminService.changePassword(1, request);

        assertEquals("newPassword", updatedEmployee.getPassword());
    }

    @Test
    void changePasswordMismatchTest() {
        EmployeeChangePasswordRequest request = new EmployeeChangePasswordRequest();
        request.setNewPassword("newsPassword");
        request.setConfirmPassword("mismatchPassword");

        assertThrows(IllegalArgumentException.class, () -> {
            adminService.changePassword(1, request);
        });
    }

    @Test
    void updateEmployeeTest() {
        // Tạo một đối tượng yêu cầu cập nhật
            EmployeeUpdateRequest updateRequest = new EmployeeUpdateRequest();
        updateRequest.setFirst_name("Nguyễn");
        updateRequest.setLast_name("An");
        updateRequest.setEmail("nguyena@example.com");
        updateRequest.setPhone("0987654321");
        updateRequest.setAddress("Hà Nội");
        updateRequest.setSalary(4500);

        // Cập nhật nhân viên
        Employee updatedEmployee = adminService.updateEmployee(3, updateRequest);

        // Kiểm tra xem thông tin đã được cập nhật thành công
        assertEquals("Nguyễn", updatedEmployee.getFirst_name());
        assertEquals("An", updatedEmployee.getLast_name());
        assertEquals("nguyena@example.com", updatedEmployee.getEmail());
        assertEquals("0987654321", updatedEmployee.getPhone());
        assertEquals("Hà Nội", updatedEmployee.getAddress());
        assertEquals(4500, updatedEmployee.getSalary());
    }

    @Test
    void updateEmployeeNotFoundTest() {
        // Tạo một đối tượng yêu cầu cập nhật cho nhân viên không tồn tại
        EmployeeUpdateRequest updateRequest = new EmployeeUpdateRequest();
        updateRequest.setFirst_name("null");
        updateRequest.setLast_name("null");
        updateRequest.setEmail("null");
        updateRequest.setPhone("0123456789");
        updateRequest.setAddress("null");
        updateRequest.setSalary(0);

        // Kiểm tra ngoại lệ khi cập nhật nhân viên không tồn tại
        assertThrows(RuntimeException.class, () -> {
            adminService.updateEmployee(99, updateRequest); // ID không tồn tại
        });
    }

    @Test
    void createBranchTest() {
        ClinicBranchCreationRequest request = new ClinicBranchCreationRequest();
        request.setBranch_address("456 Secondary St");
        request.setBranch_description("Secondary clinic branch");
        request.setBranch_img("image2.png");
        request.setBranch_phone("9876543210");
        request.setBranchName("Secondary Branch");

        Branch newBranch = adminService.createBranch(request);

        // Kiểm tra xem nhánh mới đã được tạo thành công
        assertNotNull(newBranch);
        assertEquals("456 Secondary St", newBranch.getBranch_address());
        assertEquals("Secondary clinic branch", newBranch.getBranch_description());
        assertEquals("image2.png", newBranch.getBranch_img());
        assertEquals("9876543210", newBranch.getBranch_phone());
        assertEquals("Secondary Branch", newBranch.getBranchName());
    }

    @Test
    void getAllBranchesTest() {
        List<Branch> branches = adminService.getAllBranches();

        // Kiểm tra xem danh sách nhánh có ít nhất một nhánh
        assertFalse(branches.isEmpty());

        assertEquals(1, branches.get(0).getBran_id()); // Kiểm tra nhánh đầu tiên
        assertEquals("Hai Phong's Dental",branches.get(0).getBranchName());
        assertEquals("Hai Phong", branches.get(0).getBranch_address());
        assertEquals("The dental clinic top 1 Hai Phong", branches.get(0).getBranch_description());
        assertEquals("/img/banner.png", branches.get(0).getBranch_img());
        assertEquals("0123456789", branches.get(0).getBranch_phone());
    }

    @Test
    void getBranchByIdTest() {
        Branch foundBranch = adminService.getBranchById(1);

        // Kiểm tra xem nhánh có được trả về đúng
        assertNotNull(foundBranch);
        assertEquals(1, foundBranch.getBran_id());
        assertEquals("Hai Phong", foundBranch.getBranch_address());
        assertEquals("The dental clinic top 1 Hai Phong", foundBranch.getBranch_description());
        assertEquals("/img/banner.png", foundBranch.getBranch_img());
        assertEquals("0123456789", foundBranch.getBranch_phone());
        assertEquals("Hai Phong's Dental", foundBranch.getBranchName());
    }

    @Test
    void getBranchByIdNotFoundTest() {
        // Kiểm tra ngoại lệ khi tìm kiếm nhánh không tồn tại
        assertThrows(RuntimeException.class, () -> {
            adminService.getBranchById(99); // ID không tồn tại
        });
    }

    @Test
    void updateBranchSuccessTest() {
        ClinicBranchUpdateRequest request = new ClinicBranchUpdateRequest();
        request.setBranchName("Hồ Chí Minh");
        request.setBranch_address("456 Trần Hưng Đạo, Hồ Chí Minh");
        request.setBranch_phone("0987654321");
        request.setBranch_description("Phòng khám tại Hồ Chí Minh");
        request.setBranch_img("hcm.jpg");

        Branch updatedBranch = adminService.updateBranch(2, request);

        assertEquals("Hồ Chí Minh", updatedBranch.getBranchName());
        assertEquals("456 Trần Hưng Đạo, Hồ Chí Minh", updatedBranch.getBranch_address());
        assertEquals("0987654321", updatedBranch.getBranch_phone());
        assertEquals("Phòng khám tại Hồ Chí Minh", updatedBranch.getBranch_description());
        assertEquals("hcm.jpg", updatedBranch.getBranch_img());
    }

    @Test
    void updateBranchNotFoundTest() {
        ClinicBranchUpdateRequest request = new ClinicBranchUpdateRequest();
        request.setBranchName("null");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adminService.updateBranch(99, request);  // Giả sử id = 99 không tồn tại
        });

        assertEquals("Invalid Branch ID", exception.getMessage());
    }

    @Test
    void deleteBranchSuccessTest() {
        // Đảm bảo rằng nhánh tồn tại
        adminService.deleteBranch(1);

        // Kiểm tra rằng nhánh đã bị xóa
        assertThrows(RuntimeException.class, () -> {
            adminService.getBranchById(1);  // Nhánh không tồn tại sau khi xóa
        });
    }

    @Test
    void deleteBranchNotFoundTest() {
        // Cố gắng xóa một nhánh không tồn tại
        assertThrows(RuntimeException.class, () -> {
            adminService.deleteBranch(999);  // Giả sử id = 999 không tồn tại
        });
    }
}
