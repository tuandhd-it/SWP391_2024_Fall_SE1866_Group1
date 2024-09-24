package Project.SWP391_2024_Fall_SE1866_Group1.Controller;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Employee;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Role;
import Project.SWP391_2024_Fall_SE1866_Group1.Service.ReceptionistService;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ReceptionistCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
public class GeneralController {

    @Autowired
    private ReceptionistService receptionistService;

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
        ArrayList<Role> registerRoles = new ArrayList<>();
        registerRoles.add(receptionistService.findRoleById(3));
        registerRoles.add(receptionistService.findRoleById(4));
        registerRoles.add(receptionistService.findRoleById(5));
        model.addAttribute("roles", registerRoles);
        return "register";
    }

    @PostMapping("/nextRegister")
    public String nextRegister(Model model, @RequestParam("role") String role) {
        Role choosenRole = receptionistService.findByRoleName(role);
        model.addAttribute("role", choosenRole);
        return "nextRegister";
    }

    @PostMapping("/register")
    public String registerPost(@RequestBody ReceptionistCreationRequest request) {
        receptionistService.createReceptionist(request);
        return "landing_Page";
    }

    @RequestMapping("/homePage")
    public String homePage(Model model) {
        return "landing_Page";
    }

    @RequestMapping("/editProfile")
    public String editProfile(Model model) {
        return "edit_profile";
    }
}
