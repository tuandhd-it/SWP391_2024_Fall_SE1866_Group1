package project.dental_clinic_management.service;

import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.repository.*;
import project.dental_clinic_management.dto.request.ClinicBranchCreationRequest;
import project.dental_clinic_management.dto.request.ClinicBranchUpdateRequest;
import project.dental_clinic_management.dto.request.EmployeeChangePasswordRequest;
import project.dental_clinic_management.dto.request.EmployeeUpdateRequest;
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

        employee.setFirst_name(employeeUpdateRequest.getFirst_name());
        employee.setLast_name(employeeUpdateRequest.getLast_name());
        employee.setEmail(employeeUpdateRequest.getEmail());
        employee.setPhone(employeeUpdateRequest.getPhone());
        employee.setAddress(employeeUpdateRequest.getAddress());
        employee.setGender(employeeUpdateRequest.getGender());
        employee.setDob(employeeUpdateRequest.getDob());
        employee.setSalary(employeeUpdateRequest.getSalary());
        employee.setRole(employeeUpdateRequest.getRole());
        employee.setStatus(employeeUpdateRequest.getStatus());

        return employeeRepository.save(employee);
    }

    //Add Branch to database
    public Branch createBranch(ClinicBranchCreationRequest request) {
        Branch newBranch = new Branch();

        newBranch.setBranch_address(request.getBranch_address());
        newBranch.setBranch_description(request.getBranch_description());
        newBranch.setBranch_img(request.getBranch_img());
        newBranch.setBranch_phone(request.getBranch_phone());
        newBranch.setBranchName(request.getBranchName());

        return  branchRepository.save(newBranch);
    }

    //Get Branch
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    //Get Branch by Id
    public Branch getBranchById(int id) {
        return branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid Branch ID"));
    }

    //Update Branch
    public Branch updateBranch(int id, ClinicBranchUpdateRequest updateBranchRequest) {
        Branch branch = getBranchById(id);
        branch.setBranch_address(updateBranchRequest.getBranch_address());
        branch.setBranch_description(updateBranchRequest.getBranch_description());
        branch.setBranch_img(updateBranchRequest.getBranch_img());
        branch.setBranch_phone(updateBranchRequest.getBranch_phone());
        branch.setBranchName(updateBranchRequest.getBranchName());
        return branchRepository.save(branch);
    }

    //Delete Branch
    public void deleteBranch(int id) {
        branchRepository.deleteById(id);
    }


}
