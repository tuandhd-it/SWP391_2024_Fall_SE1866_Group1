package project.dental_clinic_management.controller;

import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Employee;
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

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/branchCreate")
    public String createBranch(@RequestBody ClinicBranchCreationRequest branchRequest) {
        adminService.createBranch(branchRequest);
        return "redirect:/";
    }

    @GetMapping("/branchList")
    public String getAllBranches(Model model) {
        //Get list Branch
        List<Branch> list = adminService.getAllBranches();
        model.addAttribute("branches", list);
        return "redirect:/";
    }

    @PutMapping("/editBranch")
    public String editBranch(@RequestBody ClinicBranchUpdateRequest branchRequest, RedirectAttributes redirectAttributes) {
        adminService.updateBranch(branchRequest.getId(), branchRequest);
        redirectAttributes.addFlashAttribute("message","");
        return "redirect:/";
    }

    @GetMapping("/listEmployee")
    public String getAllEmployees(Model model) {
        List<Employee> list = adminService.getAllEmployees();
        model.addAttribute("employees", list);
        return "redirect:/";
    }

    @PutMapping("/changeEmployeePass")
    public String changeEmployeePass(@RequestBody EmployeeChangePasswordRequest employeeRequest,  RedirectAttributes redirectAttributes) {
        adminService.changePassword(employeeRequest.getId(), employeeRequest);
        redirectAttributes.addFlashAttribute("message", "Password changed successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/updateEmployee")
    public String updateEmployee(@ModelAttribute EmployeeUpdateRequest employeeRequest, RedirectAttributes redirectAttributes) {
        adminService.updateEmployee(employeeRequest.getEmp_id(), employeeRequest);
        redirectAttributes.addFlashAttribute("message", "Employee updated successfully!");
        return "redirect:/profile";
    }


}
