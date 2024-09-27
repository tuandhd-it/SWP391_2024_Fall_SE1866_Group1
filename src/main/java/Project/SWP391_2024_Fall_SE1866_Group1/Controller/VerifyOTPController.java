package Project.SWP391_2024_Fall_SE1866_Group1.Controller;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Employee;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.ForgotPassword;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.RegisterOTPVerify;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Role;
import Project.SWP391_2024_Fall_SE1866_Group1.Repository.ForgotPasswordRepository;
import Project.SWP391_2024_Fall_SE1866_Group1.Repository.RegisterOTPVerifyRepository;
import Project.SWP391_2024_Fall_SE1866_Group1.Service.EmailService;
import Project.SWP391_2024_Fall_SE1866_Group1.Service.ReceptionistService;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.MailBody;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ReceptionistCreationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;

@Controller
@RequestMapping("/verifyEmail")

//Use @SessionAttributes to store creationRequest through many web page
@SessionAttributes("creationRequest")
public class VerifyOTPController {

    private final ReceptionistService receptionistService;

    public final EmailService emailService;

    public final RegisterOTPVerifyRepository registerOTPVerifyRepository;

    public VerifyOTPController(ReceptionistService receptionistService, EmailService emailService, RegisterOTPVerifyRepository registerOTPVerifyRepository) {
        this.receptionistService = receptionistService;
        this.emailService = emailService;
        this.registerOTPVerifyRepository = registerOTPVerifyRepository;
    }

    // send mail for email verification

    @PostMapping("/verify")
    public String forgotPassword(@RequestParam("email") String email, @RequestParam("roleValue") String role, Model model, @ModelAttribute ReceptionistCreationRequest request) {
        String existed = receptionistService.checkExistedEmployee(request.getEmail(), request.getPhone());
        if (existed != null) {
            model.addAttribute("existed", existed);
            return "login";
        }

        model.addAttribute("creationRequest", request);
        model.addAttribute("roleValue", role);

        int otp = otpGenerator();

        RegisterOTPVerify rv = RegisterOTPVerify.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .email(email)
                .build();

        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This is  the OTP for your request : " + otp)
                .subject("OTP for your request")
                .build();

        emailService.sendSimpleMessage(mailBody);
        registerOTPVerifyRepository.save(rv);

        model.addAttribute("email", email);
        model.addAttribute("msg", "OTP has sent to email for verification");

        return "enterVerifyOTP";
    }

    @PostMapping("/verifyOTP")
    public String verifyOTP(@RequestParam("otp") Integer otp, @RequestParam("email") String email, @RequestParam("roleValue") String role, @ModelAttribute("creationRequest") ReceptionistCreationRequest request, Model model) {
        Employee employee = receptionistService.findByEmail(email);

        RegisterOTPVerify checkRightOTP = registerOTPVerifyRepository.findByOtp(otp);
        if (checkRightOTP == null) {
            model.addAttribute("otpMsg", "Wrong OTP");
            return "enterVerifyOTP";
        }

        RegisterOTPVerify rv = registerOTPVerifyRepository.findByOTPAndEmail(otp, email).orElseThrow(() -> new RuntimeException("This OTP not for this email"));

        if (rv.getExpirationTime().before(Date.from(Instant.now()))) {
            model.addAttribute("otpMsg", "The OTP has expired");
            registerOTPVerifyRepository.deleteById(rv.getRvid());
            return "login";
        }

        request.setAccept(false);
        request.setActive(true);
        request.setStatus("Check out");
        request.setSalary(0);
        Role choosenRole = receptionistService.findByRoleName(role);
        request.setRole(choosenRole);
        receptionistService.createReceptionist(request);
        model.addAttribute("message", "Registered successfully");
        model.addAttribute("otpMsg", "OTP verified");
        registerOTPVerifyRepository.deleteById(rv.getRvid());

        // Xóa khỏi session sau khi đã sử dụng
        model.asMap().remove("creationRequest");
        return "login";
    }


    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100000, 999999);
    }
}
