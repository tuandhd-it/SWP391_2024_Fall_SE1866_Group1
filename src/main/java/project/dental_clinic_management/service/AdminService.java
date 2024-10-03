package project.dental_clinic_management.service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.dental_clinic_management.dto.request.*;
import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.Role;
import project.dental_clinic_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RoleRepository roleRepository;

    public static void saveEmployee(Employee employee) {
    }


    //Get all Role
    public List<Role> getAllRole(){
        return roleRepository.findAll();
    }

    //Load all employee
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    //Find employee by Id
    public Employee getEmployeeById(int id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    //Change password employee
    public Employee changePassword(int id, EmployeeChangePasswordRequest employeeChangePasswordRequest) {
        Employee employee = getEmployeeById(id);

        if (!employeeChangePasswordRequest.getNewPassword().equals(employeeChangePasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        employee.setPassword(employeeChangePasswordRequest.getNewPassword());
        return employeeRepository.save(employee);
    }
    
    //Update Employee
    public Employee updateEmployee(int id, EmployeeUpdateRequest employeeUpdateRequest) {
        Employee employee = getEmployeeById(id);

        if (employee == null) {
            throw new RuntimeException("Employee with id " + id + " not found.");
        }

        employee.setFirst_name(employeeUpdateRequest.getFirst_name());
        employee.setLast_name(employeeUpdateRequest.getLast_name());
        employee.setEmail(employeeUpdateRequest.getEmail());
        employee.setPhone(employeeUpdateRequest.getPhone());
        employee.setAddress(employeeUpdateRequest.getAddress());
        employee.setGender(employeeUpdateRequest.getGender());
        employee.setDob(employeeUpdateRequest.getDob());
        employee.setSalary(employeeUpdateRequest.getSalary());
        employee.setRole(employeeUpdateRequest.getRole());
        employee.setImg(employeeUpdateRequest.getImg());
        employee.set_active(employeeUpdateRequest.isActive());

        return employeeRepository.save(employee);
    }

    public List<Employee> searchEmployeesByNameOrPhone(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return employeeRepository.findAll(); // Nếu không có từ khóa, trả về tất cả
        }
        return employeeRepository.findByNameContainingOrPhoneContaining(keyword, keyword);
    }

    public void updatePassword(int empId, String newPassword) {
        Employee employee = getEmployeeById(empId);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        employee.setPassword(encodedPassword);
        employeeRepository.save(employee);
    }

    /**
     * Add a branch to database
     * @param request a object
     * @return a ClinicBranchCreationRequest
     */
    public Branch createBranch(ClinicBranchCreationRequest request) {

        Branch newBranch = new Branch(); // create new branch and set information
        newBranch.setBranch_address(request.getBranch_address());
        newBranch.setBranch_description(request.getBranch_description());
        newBranch.setBranch_img(request.getBranch_img());
        newBranch.setBranch_phone(request.getBranch_phone());
        newBranch.setBranchName(request.getBranchName());
        newBranch.setActive(true);

        return  branchRepository.save(newBranch); //save in database
    }

    /**
     * Get all branch
     * @return a list <code>java.util.List</code>
     */
    public List<Branch> getAllBranches() {
        return branchRepository.findAll(); //Return list of branch
    }

    /**
     * Get a branch by Id
     * @param id
     * @return branch, a branch have id
     */
    public Branch getBranchById(int id) {
        return branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid Branch ID")); //return id or exception
    }

    /**
     * Update branch in database
     * @param id
     * @param updateBranchRequest
     * @return updated branch have id
     */
    public Branch updateBranch(int id, ClinicBranchUpdateRequest updateBranchRequest) {
        Branch branch = getBranchById(id);//Find the branch to modify

        if (branch == null) { //If null throw run time exception
            throw new RuntimeException("Branch with id " + id + " not found.");
        }

        //Set information need to modify
        branch.setBranch_address(updateBranchRequest.getBranch_address());
        branch.setBranch_description(updateBranchRequest.getBranch_description());
        branch.setBranch_img(updateBranchRequest.getBranch_img());
        branch.setBranch_phone(updateBranchRequest.getBranch_phone());
        branch.setBranchName(updateBranchRequest.getBranchName());
        return branchRepository.save(branch); // Return updated branch
    }

    //Delete Branch
    public void deleteBranch(int id) {
        branchRepository.deleteById(id);
    }


}
