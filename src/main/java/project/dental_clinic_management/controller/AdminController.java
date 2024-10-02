package project.dental_clinic_management.controller;

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

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/branchCreate")
    public String createBranch(@ModelAttribute ClinicBranchCreationRequest branchRequest) {
        adminService.createBranch(branchRequest);
        return "redirect:/admin/manageBranchs";
    }

    @GetMapping("/manageBranchs")
    public String getAllBranches(Model model) {
        //Get list Branch
        List<Branch> list = adminService.getAllBranches();
        model.addAttribute("branches", list);
        ClinicBranchUpdateRequest updateRequest = new ClinicBranchUpdateRequest();
        model.addAttribute("updateBranch", updateRequest);
        ClinicBranchCreationRequest creationRequest = new ClinicBranchCreationRequest();
        model.addAttribute("createBranch", creationRequest);
        return "/manageBranch";
    }

    @PostMapping("/editBranch")
    public String editBranch(@ModelAttribute ClinicBranchUpdateRequest branchRequest, RedirectAttributes redirectAttributes) {
        adminService.updateBranch(branchRequest.getId(), branchRequest);
        redirectAttributes.addFlashAttribute("message","");
        return "redirect:/admin/manageBranchs";
    }

    @GetMapping("/manageAcc")
    public String getAllEmployees(Model model) {
        List<Employee> list = adminService.getAllEmployees();
        model.addAttribute("employees", list);
        return "manageAcc";
    }

    @GetMapping("/manageEmp")
    public String getAllEmployeesInfo(Model model) {
        List<Employee> list = adminService.getAllEmployees();
        model.addAttribute("employees", list);
        return "manageEmp";
    }

    @GetMapping("employeesDetails/{id}")
    public String showEmployeeDetails(@PathVariable("id") Integer empId, Model model) {
        Employee employee = adminService.getEmployeeById(empId);
        List<Branch> listBranch = adminService.getAllBranches();
        List<Role> listRole = adminService.getAllRole();
        EmployeeUpdateRequest editEmployees =  new EmployeeUpdateRequest();
        model.addAttribute("employee", employee);
        model.addAttribute("listBranches", listBranch);
        model.addAttribute("listRoles", listRole);
        model.addAttribute("editEmployees", editEmployees);
        return "detailsEmp";
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
        return "/manageEmp";
    }


}
