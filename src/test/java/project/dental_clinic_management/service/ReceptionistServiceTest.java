package project.dental_clinic_management.service;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import project.dental_clinic_management.dto.request.ReceptionistCreationRequest;
import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.Role;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReceptionistServiceTest {

    @Autowired
    private ReceptionistService receptionistService;

    @Test
    public void testFindByUsername_ExistingUsername() {
        // Tìm nhân viên bằng email tồn tại
        String username = "tuan6a1thcstv@gmail.com";
        Employee foundEmployee = receptionistService.findByUsername(username);

        // Kiểm tra kết quả
        assertNotNull(foundEmployee, "Employee should not be null");
        assertEquals(username, foundEmployee.getEmail(), "Emails should match");
        assertEquals("Đỗ", foundEmployee.getFirst_name(), "First names should match");
        assertEquals("Tuấn", foundEmployee.getLast_name(), "Last names should match");
    }

    @Test
    public void testFindByUsername_NonExistingUsername() {
        // Tìm nhân viên bằng email không tồn tại
        String nonExistingUsername = "nonexistent@example.com";
        Employee foundEmployee = receptionistService.findByUsername(nonExistingUsername);

        // Kiểm tra kết quả
        assertNull(foundEmployee, "Employee should be null for non-existing username");
    }

    @Test
    public void testFindRoleById() {
        // Lấy Role từ cơ sở dữ liệu

        Role foundRole = receptionistService.findRoleById(2);

        // Kiểm tra kết quả
        assertNotNull(foundRole, "Role should not be null");
        assertEquals(2, foundRole.getRole_id(), "Role IDs should match");
        assertEquals("Manager", foundRole.getRoleName(), "Role names should match");
    }

    @Test
    public void testFindRoleById_NonExistingId_ThrowsException() {
        // Sử dụng một ID không tồn tại
        int nonExistingId = 99; // Giả định rằng ID âm không tồn tại trong cơ sở dữ liệu

        // Kiểm tra ngoại lệ được ném ra
        Role role =  receptionistService.findRoleById(nonExistingId);
        assertNull(role, "Role should be null");
    }

    @Test
    public void testFindByRoleName() {
        // Kiểm tra tìm kiếm role theo tên
        String roleName = "Admin";
        Role foundRole = receptionistService.findByRoleName(roleName);

        // Kiểm tra kết quả
        assertNotNull(foundRole, "Role should not be null");
        assertEquals(roleName, foundRole.getRoleName(), "Role names should match");
    }

    @Test
    public void testFindByRoleName_NonExistingRole() {
        // Tên role không tồn tại
        String nonExistingRoleName = "NON_EXISTING_ROLE";
        Role foundRole = receptionistService.findByRoleName(nonExistingRoleName);

        // Kiểm tra kết quả
        assertNull(foundRole, "Role should be null for non-existing role name");
    }

    @Test
    public void testFindAllBranches() {
        // Kiểm tra tìm tất cả các chi nhánh
        List<Branch> branches = receptionistService.findAllBranches();

        // Kiểm tra kết quả
        assertNotNull(branches, "Branches list should not be null");
        assertFalse(branches.isEmpty(), "Branches list should not be empty");
        assertEquals("Hai Phong's Dental", branches.get(0).getBranchName(), "Branch names should match");
    }

    @Test
    public void testFindByPhone() {
        // Tìm nhân viên bằng số điện thoại
        String phone = "0123456789";
        Employee foundEmployee = receptionistService.findByPhone(phone);

        // Kiểm tra kết quả
        assertNotNull(foundEmployee, "Employee should not be null");
        assertEquals(phone, foundEmployee.getPhone(), "Phone numbers should match");
        assertEquals("Đỗ", foundEmployee.getFirst_name(), "First names should match");
        assertEquals("Tuấn", foundEmployee.getLast_name(), "Last names should match");
    }

    @Test
    public void testFindByPhone_NonExistingPhone() {
        // Tên role không tồn tại
        String nonExistingRoleName = "11111111111";
        Employee foundRole = receptionistService.findByPhone(nonExistingRoleName);

        // Kiểm tra kết quả
        assertNull(foundRole);
    }


    @Test
    public void     testCheckExistedEmployee_EmailExists() {
        // Giả sử có một nhân viên với email đã tồn tại trong cơ sở dữ liệu
        String existingEmail = "tuan6a1thcstv@gmail.com";
        String phone = "123456789"; // Số điện thoại có thể tồn tại hoặc không

        String result = receptionistService.checkExistedEmployee(existingEmail, phone);

        assertEquals("Email already exists", result, "Expected message for existing email");
    }

    @Test
    public void testCheckExistedEmployee_PhoneExists() {
        String existingEmail = "tuan6a1thc@gmail.com";
        String phone = "0123456789"; // Số điện thoại có thể tồn tại hoặc không

        String result = receptionistService.checkExistedEmployee(existingEmail, phone);

        assertEquals("Phone already exists", result, "Expected message for existing email");
    }

    @Test
    public void testCheckExistedEmployee_NeitherExists() {
        // Cả email và số điện thoại đều không tồn tại
        String email = "new@example.com";
        String phone = "987654321";

        // Kiểm tra rằng không có nhân viên nào trong cơ sở dữ liệu với email và số điện thoại này
        String result = receptionistService.checkExistedEmployee(email, phone);

        assertNull(result, "Expected null when neither email nor phone exists");
    }
}
