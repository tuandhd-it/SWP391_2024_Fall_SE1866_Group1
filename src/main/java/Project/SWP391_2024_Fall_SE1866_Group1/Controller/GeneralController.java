package Project.SWP391_2024_Fall_SE1866_Group1.Controller;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.*;
import Project.SWP391_2024_Fall_SE1866_Group1.Service.DoctorService;
import Project.SWP391_2024_Fall_SE1866_Group1.Service.ReceptionistService;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.DoctorCreationRequest;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ReceptionistCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class GeneralController {

    @Autowired
    private ReceptionistService receptionistService;

    @Autowired
    private DoctorService doctorService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/profile")
    public String loginPost(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Employee employee = receptionistService.findByUsername(username);
        model.addAttribute("employee", employee);
        return "profile";
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
        if (role.equalsIgnoreCase("Receptionist")) {
            ReceptionistCreationRequest receptionistCreationRequest = new ReceptionistCreationRequest();
            model.addAttribute("request", receptionistCreationRequest);
            return "nextRegister";
        } else {
            DoctorCreationRequest doctorCreationRequest = new DoctorCreationRequest();
            model.addAttribute("doctorRequest", doctorCreationRequest);
            return "nextRegisterDoctor";
        }
    }

    @PostMapping("/registerDoctor")
    public String registerPost(@ModelAttribute DoctorCreationRequest request,
                               @RequestParam("roleValue") String role,
                               Model model) {
        String existed = receptionistService.checkExistedEmployee(request.getEmail(), request.getPhone());
        if (existed != null) {
            model.addAttribute("existed", existed);
        } else {
            request.setAccept(false);
            request.setActive(true);
            request.setStatus("Check out");
            request.setSalary(0);
            Role choosenRole = doctorService.findByRoleName(role);
            request.setRole(choosenRole);
            doctorService.createDoctor(request);
            model.addAttribute("message", "Registered successfully");
        }
        return "login";
    }

    @PostMapping("/register")
    public String registerDoctorPost(@ModelAttribute ReceptionistCreationRequest request, @RequestParam("roleValue") String role, Model model) {
        String existed = receptionistService.checkExistedEmployee(request.getEmail(), request.getPhone());
        if (existed != null) {
            model.addAttribute("existed", existed);
        } else {
            request.setAccept(false);
            request.setActive(true);
            request.setStatus("Check out");
            request.setSalary(0);
            Role choosenRole = receptionistService.findByRoleName(role);
            request.setRole(choosenRole);
            receptionistService.createReceptionist(request);
            model.addAttribute("message", "Registered successfully");
        }
        return "login";
    }

    @RequestMapping("/homePage")
    public String homePage(Model model,  @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {
            // Người dùng đã đăng nhập
            model.addAttribute("loginSuccess", userDetails.getUsername());
            Employee employee = receptionistService.findByUsername(userDetails.getUsername());
            model.addAttribute("employee", employee);
        }
        return "landing_Page";
    }


    @GetMapping("/changePass")
    public String changePass(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        // You can also add employee information here if needed
        String username = userDetails.getUsername();
        Employee employee = receptionistService.findByUsername(username);
        model.addAttribute("employee", employee);
        return "changePass";
    }

}
