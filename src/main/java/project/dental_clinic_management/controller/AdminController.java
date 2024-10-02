package project.dental_clinic_management.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import project.dental_clinic_management.dto.request.*;
import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.service.AdminService;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminService adminService;

    @PostMapping("/branchCreate")
    public String createBranch(@ModelAttribute ClinicBranchCreationRequest branchRequest) {
        adminService.createBranch(branchRequest);
        return "redirect:/";
    }

    @GetMapping("/manageBranch")
    public String getAllBranches(Model model) {
        //Get list Branch
        List<Branch> list = adminService.getAllBranches();
        model.addAttribute("branches", list);
        ClinicBranchCreationRequest request = new ClinicBranchCreationRequest();
        model.addAttribute("request", request);
        return "/manageBranch";
    }

    @PutMapping("/editBranch")
    public String editBranch(@ModelAttribute ClinicBranchUpdateRequest branchRequest, RedirectAttributes redirectAttributes) {
        adminService.updateBranch(branchRequest.getId(), branchRequest);
        redirectAttributes.addFlashAttribute("message","");
        return "/";
    }

    @GetMapping("/manageAcc")
    public String getAllEmployees(Model model) {
        List<Employee> list = adminService.getAllEmployees();
        model.addAttribute("employees", list);
        return "manageAcc";
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
        return "manageAcc";
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
        return "redirect:/manageAcc";
    }
}
