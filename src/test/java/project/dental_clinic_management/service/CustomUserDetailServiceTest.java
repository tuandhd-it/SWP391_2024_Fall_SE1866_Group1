package project.dental_clinic_management.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import project.dental_clinic_management.entity.Employee;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
public class CustomUserDetailServiceTest {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Test
    void loadUserByUsernameSuccessTest() {
        // Gọi phương thức loadUserByUsername
        UserDetails userDetails = customUserDetailService.loadUserByUsername("nguyena@example.com");

        // Kiểm tra kết quả trả về không null
        assertNotNull(userDetails);
        assertEquals("nguyena@example.com", userDetails.getUsername());
    }

    @Test
    void loadUserByUsernameNotFoundTest() {
        // Gọi phương thức với một `username` không tồn tại
        assertThrows(RuntimeException.class, () -> {
            customUserDetailService.loadUserByUsername("notfound@gmail.com");
        });
    }

    @Test
    void findByUsernameSuccessTest() {
        // Tìm kiếm nhân viên theo email
        Employee foundEmployee = customUserDetailService.findByUsername("nguyena@example.com");

        // Kiểm tra các thuộc tính
        assertEquals("Nguyễn", foundEmployee.getFirst_name());
        assertEquals("An", foundEmployee.getLast_name());
        assertEquals("nguyena@example.com", foundEmployee.getEmail());
        assertEquals("0987654321", foundEmployee.getPhone());
        assertEquals(LocalDate.of(2004, 5, 10), foundEmployee.getDob());
        assertEquals("Male", foundEmployee.getGender());
        assertEquals("Hà Nội", foundEmployee.getAddress());
        assertEquals(4500, foundEmployee.getSalary());
    }

    @Test
    void findByUsernameNotFoundTest() {
        // Tìm kiếm nhân viên không tồn tại
        Employee foundEmployee = customUserDetailService.findByUsername("notfound@gmail.com");

        // Nhân viên không tồn tại, kết quả sẽ là null
        assertNull(foundEmployee);
    }


}
