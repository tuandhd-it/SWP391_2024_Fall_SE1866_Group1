
package project.dental_clinic_management.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    //Tra ve trang login
    @GetMapping("/login")
    public String login() {
        return "/auth/login";
    }

    //Lay du kieu cac role co the dang ky cho trang register
    @GetMapping("/register")
    public String register(Model model) {
        // Retreat role that can register
        List<Role> registerRoles = new ArrayList<>();
        registerRoles.add(receptionistService.findRoleById(3));
        registerRoles.add(receptionistService.findRoleById(4));
        registerRoles.add(receptionistService.findRoleById(5));
        model.addAttribute("roles", registerRoles);
        return "/auth/register";
    }

    //Lay du lieu cac branch cho trang nhap thong tin de dang ky
    @PostMapping("/nextRegister")
    public String nextRegister(Model model, @RequestParam("role") String role) {
        model.addAttribute("roleValue", role);
        List<Branch> branches = receptionistService.findAllBranches();
        model.addAttribute("branches", branches);
        ReceptionistCreationRequest receptionistCreationRequest = new ReceptionistCreationRequest();
        model.addAttribute("request", receptionistCreationRequest);
        return "/auth/nextRegister";
    }

    //Check xem nguoi truy cap vao home page da dang nhap chua
    @RequestMapping("/homePage")
    public String homePage(Model model,  @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails != null) {

            model.addAttribute("loginSuccess", userDetails.getUsername());
            Employee employee = receptionistService.findByUsername(userDetails.getUsername());
            model.addAttribute("employee", employee);
        }
        return "LandingPage";
    }


    @GetMapping("/changePass")
    public String changePass(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Employee employee = receptionistService.findByUsername(username);
        model.addAttribute("employee", employee);
        return "/user/changePass";
    }

    @PostMapping("/changePass")
    public String changePass(@RequestParam String currentPassword,
                             @RequestParam String newPassword,
                             @RequestParam String confirmNewPassword,
                             RedirectAttributes redirectAttributes,
                             @AuthenticationPrincipal UserDetails userDetails,
                             Model model) {
    try {
        String username = userDetails.getUsername();
        Employee employee = receptionistService.findByUsername(username);
        if (!passwordEncoder.matches(currentPassword, employee.getPassword())) {
            model.addAttribute("error", "Hãy nhập đúng mật khẩu cũ");
            return "/user/changePass"; // Quay lại trang đổi mật khẩu nếu sai
        }


        if (!newPassword.equals(confirmNewPassword)) {
            model.addAttribute("error", "Mật khẩu không trùng khớp");
            return "/user/changePass";
        }

        employee.setPassword(passwordEncoder.encode(newPassword));
        customUserDetailService.saveEmployee(employee);

        redirectAttributes.addFlashAttribute("messageChange", "Thay đổi mật khẩu thành công");
    } catch (Exception e) {
        model.addAttribute("error", "An error occurred: " + e.getMessage());
        return "/user/changePass";
    }
    return "/user/profile";
}

    @GetMapping("/profile")
    public String viewProfile(Model model, @AuthenticationPrincipal UserDetails userDetails,@ModelAttribute("messageUpdate") String messageUpdate) {
        String username = userDetails.getUsername();
        Employee employee = customUserDetailService.findByUsername(username);
        model.addAttribute("employee", employee);
        if (messageUpdate != null && !messageUpdate.isEmpty()) {
            model.addAttribute("messageUpdate", messageUpdate);
        }
        model.addAttribute("editMode", false);
        return "/user/profile";
    }

        @PostMapping("/profile/update")
        public String updateProfile(@Valid @ModelAttribute("employee") Employee employee,
                                    BindingResult bindingResult,
                                    RedirectAttributes redirectAttributes,
                                    @AuthenticationPrincipal UserDetails userDetails,
                                    Model model) {

            if (bindingResult.hasErrors()) {
                model.addAttribute("editMode", true);
                return "/user/profile";
            }


            String username = userDetails.getUsername();
            Employee existingEmployee = customUserDetailService.findByUsername(username);

            if (existingEmployee != null) {
                existingEmployee.setFirst_name(employee.getFirst_name());
                existingEmployee.setLast_name(employee.getLast_name());
                existingEmployee.setEmail(employee.getEmail());
                existingEmployee.setPhone(employee.getPhone());
                existingEmployee.setDob(employee.getDob());
                existingEmployee.setGender(employee.getGender());
                existingEmployee.setAddress(employee.getAddress());
                customUserDetailService.saveEmployee(existingEmployee);
                redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công!!");
            }

            return "/user/profile";
        }


}
