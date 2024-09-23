package Project.SWP391_2024_Fall_SE1866_Group1.Controller;

import Project.SWP391_2024_Fall_SE1866_Group1.Service.ReceptionistService;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ReceptionistCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GeneralController {

    @Autowired
    private ReceptionistService receptionistService;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/profile")
    public String profile(Model model) {

        return "profile";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
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
