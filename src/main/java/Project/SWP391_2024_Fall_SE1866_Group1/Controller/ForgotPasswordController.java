package Project.SWP391_2024_Fall_SE1866_Group1.Controller;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Employee;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.ForgotPassword;
import Project.SWP391_2024_Fall_SE1866_Group1.Repository.EmployeeRepository;
import Project.SWP391_2024_Fall_SE1866_Group1.Repository.ForgotPasswordRepository;
import Project.SWP391_2024_Fall_SE1866_Group1.Service.EmailService;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.MailBody;
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
