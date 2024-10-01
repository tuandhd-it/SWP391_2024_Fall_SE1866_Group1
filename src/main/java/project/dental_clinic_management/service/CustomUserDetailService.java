package project.dental_clinic_management.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.dental_clinic_management.dto.request.EmployeeChangePasswordRequest;
import project.dental_clinic_management.entity.CustomEmployeeDetails;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.repository.EmployeeRepository;
import project.dental_clinic_management.dto.request.EmployeeUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private ReceptionistService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomUserDetailService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    //Custom login retreat information from database and to assign authority
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee employee = employeeService.findByUsername(username);
        if (employee == null) {
            throw new UsernameNotFoundException("Not found Employee with username: " + username);
        }
        Collection<GrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority(employee.getRole().getRoleName()));

        return new CustomEmployeeDetails(employee, authorities);
    }
    public Employee findByUsername(String username) {
        return employeeRepository.findByEmail(username);
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public void updateEmployee(EmployeeUpdateRequest employeeUpdateRequest) {
        // Tìm Employee theo emp_id (nếu bạn có ý định cập nhật theo id, nếu không bạn có thể dùng username)
        Employee employee = employeeRepository.findById(employeeUpdateRequest.getEmp_id())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Cập nhật thông tin của Employee từ EmployeeUpdateRequest
        employee.setFirst_name(employeeUpdateRequest.getFirst_name());
        employee.setLast_name(employeeUpdateRequest.getLast_name());
        employee.setEmail(employeeUpdateRequest.getEmail());
        employee.setPhone(employeeUpdateRequest.getPhone());
        employee.setDob(employeeUpdateRequest.getDob());
        employee.setGender(employeeUpdateRequest.getGender());
        employee.setAddress(employeeUpdateRequest.getAddress());
        employee.setSalary(employeeUpdateRequest.getSalary());

        // Nếu role có thay đổi, cập nhật Role
        if (employeeUpdateRequest.getRole() != null) {
            employee.setRole(employeeUpdateRequest.getRole());
        }

        // Lưu lại thông tin Employee sau khi cập nhật
        employeeRepository.save(employee);
    }





}
