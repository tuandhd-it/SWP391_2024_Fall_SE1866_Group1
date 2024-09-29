
package project.dental_clinic_management.controller;

import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.service.CustomUserDetailService;
import project.dental_clinic_management.service.ReceptionistService;
import project.dental_clinic_management.dto.request.ReceptionistCreationRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GeneralController {

    @Autowired
    private ReceptionistService receptionistService;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        // Retreat role that can register
        List<Role> registerRoles = new ArrayList<>();
        registerRoles.add(receptionistService.findRoleById(3));
        registerRoles.add(receptionistService.findRoleById(4));
        registerRoles.add(receptionistService.findRoleById(5));
        model.addAttribute("roles", registerRoles);
        return "register";
    }

    @PostMapping("/nextRegister")
    public String nextRegister(Model model, @RequestParam("role") String role) {
        model.addAttribute("roleValue", role);
        List<Branch> branches = new ArrayList<>();
        branches = receptionistService.findAllBranches();
        model.addAttribute("branches", branches);
        ReceptionistCreationRequest receptionistCreationRequest = new ReceptionistCreationRequest();
        model.addAttribute("request", receptionistCreationRequest);
        return "nextRegister";
    }

    @RequestMapping("/homePage")
    public String homePage(Model model,  @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            // Người dùng đã đăng nhập
            model.addAttribute("loginSuccess", userDetails.getUsername());
            Employee employee = receptionistService.findByUsername(userDetails.getUsername());
            model.addAttribute("employee", employee);
        }
        return "LandingPage";
    }


    @GetMapping("/changePass")
    public String changePass(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // You can also add employee information here if needed
        String username = userDetails.getUsername();
        Employee employee = receptionistService.findByUsername(username);
        model.addAttribute("employee", employee);
        return "changePass";
    }
    @GetMapping("/manageAcc")
    public String manageAcc(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // You can also add employee information here if needed
        String username = userDetails.getUsername();
        Employee employee = receptionistService.findByUsername(username);
        model.addAttribute("employee", employee);
        return "manageAcc";
    }
    @GetMapping("/profile")
    public String viewProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Employee employee = customUserDetailService.findByUsername(username);
        model.addAttribute("employee", employee);
        model.addAttribute("editMode", false); // Initial load without edit mode
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@Valid @ModelAttribute("employee") Employee employee,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true); // Keep in edit mode if there are errors
            return "profile"; // Return the profile page with validation errors
        }

        // Get the logged-in user's current employee details
        String username = userDetails.getUsername();
        Employee existingEmployee = customUserDetailService.findByUsername(username);

        if (existingEmployee != null) {
            // Update existing employee details
            existingEmployee.setFirst_name(employee.getFirst_name());
            existingEmployee.setLast_name(employee.getLast_name());
            existingEmployee.setEmail(employee.getEmail());
            existingEmployee.setPhone(employee.getPhone());
            existingEmployee.setDob(employee.getDob());
            existingEmployee.setGender(employee.getGender());
            existingEmployee.setAddress(employee.getAddress());

            // Save the updated employee
            customUserDetailService.saveEmployee(existingEmployee);

            redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");
        }

        return "redirect:/profile"; // Redirect to avoid resubmission
    }


}
