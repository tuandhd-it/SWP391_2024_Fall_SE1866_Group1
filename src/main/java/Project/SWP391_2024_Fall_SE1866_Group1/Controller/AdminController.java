package Project.SWP391_2024_Fall_SE1866_Group1.Controller;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Branch;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Employee;
import Project.SWP391_2024_Fall_SE1866_Group1.Service.AdminService;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ClinicBranchCreationRequest;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ClinicBranchUpdateRequest;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.EmployeeChangePasswordRequest;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.EmployeeUpdateRequest;
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
        return "redirect:/";
    }

    @PutMapping("/updateEmployee")
    public String updateEmployee(@RequestBody EmployeeUpdateRequest employeeRequest, RedirectAttributes redirectAttributes) {
        adminService.updateEmployee(employeeRequest.getEmp_id(), employeeRequest);
        redirectAttributes.addFlashAttribute("message", "Employee updated successfully!");
        return "redirect:/";
    }

}
