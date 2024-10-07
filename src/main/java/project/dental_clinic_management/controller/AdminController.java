package project.dental_clinic_management.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import project.dental_clinic_management.dto.request.*;
import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.Role;
import project.dental_clinic_management.service.AdminService;
import project.dental_clinic_management.dto.request.ClinicBranchCreationRequest;
import project.dental_clinic_management.dto.request.ClinicBranchUpdateRequest;
import project.dental_clinic_management.dto.request.EmployeeChangePasswordRequest;
import project.dental_clinic_management.dto.request.EmployeeUpdateRequest;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 *  Copyright(C) 2005, Group 1 - SE1864
 *  Dental Clinic Management
 *
 *  Record of change
 *  Date        Version     Author              Description
 *  2024-09-20  1.0         Nguyen Viet Lam     Admin controller
 */
@Controller
@RequestMapping("/admin")

/**
 * The class contain method to navigate to show information
 * Staff table in database. In the update or insert method, all data will be normalized (trim space) before update/insert into database
 * The methods will navigate to a error page if it have error
 *
 * @author: Nguyen Viet Lam
 */
public class AdminController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminService adminService;

    /**
     * Create a branch and add it in database and redirect to specified page
     * @param branchRequest
     * @return a url <code>java.lang.String</code>
     */
    @PostMapping("/branchCreate")
    public String createBranch(@ModelAttribute ClinicBranchCreationRequest branchRequest) {
        adminService.createBranch(branchRequest); //Create branch
        return "redirect:/admin/manageBranchs";
    }

    /**
     * Get a list of branchs and send models to specified page
     * @param model, it is <code>org.springframework.ui.Model</code>
     * @return a url <code>java.lang.String</code>
     */
    @GetMapping("/manageBranchs")
    public String getAllBranches(Model model) {
        //Get list Branch
        List<Branch> list = adminService.getAllBranches();
        model.addAttribute("branches", list);//Add in model
        ClinicBranchUpdateRequest updateRequest = new ClinicBranchUpdateRequest(); // Create a object to update branch
        model.addAttribute("updateBranch", updateRequest); //Add in model
        ClinicBranchCreationRequest creationRequest = new ClinicBranchCreationRequest();// Create a object to create branch
        model.addAttribute("createBranch", creationRequest); //Add in model
        return "/branch/manageBranch";
    }

    /**
     * Edit the specified branch and navigate to
     * @param branchRequest
     * @return a url <code>java.lang.String</code>
     */
    @PostMapping("/editBranch")
    public String editBranch(@ModelAttribute ClinicBranchUpdateRequest branchRequest) {
        adminService.updateBranch(branchRequest.getId(), branchRequest); // Update branch
        return "redirect:/admin/manageBranchs";
    }

    @GetMapping("/manageAcc")
    public String getAllEmployees(Model model) {
        List<Employee> list = adminService.getAllEmployees();
        model.addAttribute("employees", list);
        return "/employee/manageAcc";
    }

    @GetMapping("/manageEmp")
    public String getAllEmployeesInfo(Model model) {
        List<Employee> list = adminService.getAllEmployees();
        model.addAttribute("employees", list);
        return "/employee/manageEmp";
    }

    @GetMapping("employeesDetails/{id}")
    public String showEmployeeDetails(@PathVariable("id") Integer empId, Model model) {
        List<Branch> listBranch = adminService.getAllBranches();
        List<Role> listRole = adminService.getAllRole();

        //Get Employee to fill data to EmployeeRequest
        Employee employee = adminService.getEmployeeById(empId);
        EmployeeUpdateRequest employeeUpdateRequest = new EmployeeUpdateRequest();
        employeeUpdateRequest.setEmp_id(empId);
        employeeUpdateRequest.setFirst_name(employee.getFirst_name());
        employeeUpdateRequest.setLast_name(employee.getLast_name());
        employeeUpdateRequest.setEmail(employee.getEmail());
        employeeUpdateRequest.setPhone(employee.getPhone());
        employeeUpdateRequest.setDob(employee.getDob());
        employeeUpdateRequest.setGender(employee.getGender());
        employeeUpdateRequest.setAddress(employee.getAddress());
        employeeUpdateRequest.setImg(employee.getImg());
        employeeUpdateRequest.setActive(employee.is_active());
        employeeUpdateRequest.setSalary(employee.getSalary());
        employeeUpdateRequest.setBranch_id(employee.getBranch().getBran_id());
        employeeUpdateRequest.setDescription(employee.getDescription());
        employeeUpdateRequest.setRole(employee.getRole());
        model.addAttribute("employee", employee);
        model.addAttribute("listBranches", listBranch);
        model.addAttribute("listRoles", listRole);
        model.addAttribute("editEmployees", employeeUpdateRequest);
        return "/employee/detailsEmp";
    }



    @PutMapping("/changeEmployeePass")
    public String changeEmployeePass(@ModelAttribute EmployeeChangePasswordRequest employeeRequest,  RedirectAttributes redirectAttributes) {
        adminService.changePassword(employeeRequest.getEmp_id(), employeeRequest);
        redirectAttributes.addFlashAttribute("message", "Password changed successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/updateEmployee")
    public String updateEmployee(@ModelAttribute EmployeeUpdateRequest employeeRequest, RedirectAttributes redirectAttributes) {
        adminService.updateEmployee(employeeRequest.getEmp_id(), employeeRequest);
        redirectAttributes.addFlashAttribute("message", "Employee updated successfully!");
        return "redirect:/profile";
    }
    @GetMapping("/searchEmployees")
    public String searchEmployees(@RequestParam("keyword") String keyword, Model model) {
        List<Employee> employees = adminService.searchEmployeesByNameOrPhone(keyword);
        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword);
        return "/employee/manageAcc";
    }

    @GetMapping("/searchAccount")
    public String searchAccount(@RequestParam("keyword") String keyword, Model model) {
        // Tìm kiếm nhân viên theo từ khóa
        List<Employee> employees = adminService.searchEmployeesByNameOrId(keyword);
        model.addAttribute("employees", employees);
        model.addAttribute("keyword", keyword); // Để giữ lại từ khóa trong ô tìm kiếm
        return "/employee/manageEmp"; // Trả về view danh sách nhân viên
    }



    @GetMapping("/editEmployee/{id}")
    public String editEmployee(@PathVariable("id") int id, Model model) {
        Employee employee = adminService.getEmployeeById(id);
        model.addAttribute("employee", employee);
        return "editEmployeePassword";
    }

    // Cập nhật mật khẩu
    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("emp_id") int empId, @RequestParam("password") String newPassword, RedirectAttributes redirectAttributes) {
        adminService.updatePassword(empId, newPassword);
        redirectAttributes.addFlashAttribute("message", "Password updated successfully!");
        return "redirect:/admin/manageAcc";
    }
}
