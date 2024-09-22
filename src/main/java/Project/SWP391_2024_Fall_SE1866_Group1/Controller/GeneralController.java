package Project.SWP391_2024_Fall_SE1866_Group1.Controller;

import Project.SWP391_2024_Fall_SE1866_Group1.Service.ReceptionistService;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ReceptionistCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class GeneralController {

    @Autowired
    private ReceptionistService receptionistService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/profile")
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
        return "landing_page";
    }
}
