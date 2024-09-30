package project.dental_clinic_management.controller;

import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.ForgotPassword;
import project.dental_clinic_management.repository.EmployeeRepository;
import project.dental_clinic_management.repository.ForgotPasswordRepository;
import project.dental_clinic_management.service.EmailService;
import project.dental_clinic_management.dto.MailBody;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    public ForgotPasswordController(EmployeeRepository employeeRepository, EmailService emailService, ForgotPasswordRepository forgotPasswordRepository) {
        this.employeeRepository = employeeRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
    }

    private final EmployeeRepository employeeRepository;

    public final EmailService emailService;

    public final ForgotPasswordRepository forgotPasswordRepository;

    @GetMapping("/enterEmail")
    public String enterEmail() {
        return "enterEmail";
    }

    @PostMapping("/verifyOTP")
    public String verifyOTP(@RequestParam("otp") Integer otp, @RequestParam("email") String email, Model model) {
        Employee employee = employeeRepository.findByEmail(email);
        ForgotPassword checkRightOTP = forgotPasswordRepository.findByOtp(otp);
        model.addAttribute("email", email);
        if(checkRightOTP == null) {
            model.addAttribute("msg", "Wrong OTP");
            model.addAttribute("otp", otp);
            return "enterForgotOTP";
        }

        ForgotPassword fp = forgotPasswordRepository.findByEmployeeAndOtp(employee, otp);
        if(fp.getExpirationTime().before(Date.from(Instant.now()))) {
            model.addAttribute("msg", "The OTP has expired");
            forgotPasswordRepository.deleteById(fp.getFpid());
            return "enterEmail";
        }
        model.addAttribute("msg", "OTP verified");
        forgotPasswordRepository.deleteById(fp.getFpid());
        return "newPassword";
    }

    @PostMapping("/verify")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        Employee employee = employeeRepository.findByEmail(email);

        if(employee == null) {
            model.addAttribute("emailError", "This email does not exist");
            return "enterEmail";
        }

        int otp = otpGenerator();

        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .employee(employee)
                .build();

        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is  the OTP for your Forgot Password Request : " + otp)
                .subject("OTP for your request")
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);
        model.addAttribute("email", email);
        model.addAttribute("msg", "OTP has sent to email for verification");

        return "enterForgotOTP";
    }

    @PostMapping("/changePassword")
    public String changePass(@RequestParam("password") String newPassword, @RequestParam("email") String email, Model model) {
        String passwordEncode = new BCryptPasswordEncoder().encode(newPassword);
        employeeRepository.updatePassword(email, passwordEncode);

        model.addAttribute("msgChangePass", "Password has been changed");
        return "login";
    }

    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100000, 999999);
    }
}
