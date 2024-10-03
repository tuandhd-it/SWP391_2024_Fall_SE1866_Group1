package project.dental_clinic_management.service;

import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.Role;
import project.dental_clinic_management.repository.*;
import project.dental_clinic_management.dto.request.ReceptionistCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceptionistService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RoleRepository roleRepository;


    public Employee findByUsername(String username) {
        return employeeRepository.findByEmail(username);
    }

    //Create a new receptionist
    public void createReceptionist(ReceptionistCreationRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Employee employee = new Employee();

        //Create branch to store in receptionist information
        Branch branch = branchRepository.findByBranchName(request.getBranch_name());

        //Create account to store in receptionist information
        String password = encoder.encode(request.getPassword());
        employee.setPassword(password);
        employee.setDob(request.getDob());
        employee.setGender(request.getGender());
        employee.set_active(request.isActive());
        employee.setFirst_name(request.getFirst_name());
        employee.setLast_name(request.getLast_name());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setAddress(request.getAddress());
        employee.setSalary(request.getSalary());
        employee.setImg(request.getImg());
        employee.setDescription(request.getDescription());
        employee.setBranch(branch);
        employee.setRole(request.getRole());
        employeeRepository.save(employee);
    }

    //Find Role by role_id
    public Role findRoleById(int id) {
        return roleRepository.findById(id);
    }

    //Find Role by roleName
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    //Find all branch
    public List<Branch> findAllBranches() {
        return branchRepository.findAll();
    }

    //Find Employee has specific email
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    //Find Employee has specific phone number
    public Employee findByPhone(String phone) {
        return employeeRepository.findByPhone(phone);
    }

    //Check whether entered email, phone is existed
    public String checkExistedEmployee(String email, String phone) {
        Employee existedEmail = findByEmail(email);
        Employee existedPhone = findByPhone(phone);
        if (existedEmail != null) {
            return "Email already exists";
        } else if (existedPhone != null) {
            return "Phone already exists";
        }
        return null;
    }



}
