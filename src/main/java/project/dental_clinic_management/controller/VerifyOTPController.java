package project.dental_clinic_management.controller;

import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.RegisterOTPVerify;
import project.dental_clinic_management.entity.Role;
import project.dental_clinic_management.repository.RegisterOTPVerifyRepository;
import project.dental_clinic_management.service.EmailService;
import project.dental_clinic_management.service.ReceptionistService;
import project.dental_clinic_management.dto.MailBody;
import project.dental_clinic_management.dto.request.ReceptionistCreationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
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
            model.addAttribute("otp", otp);
            model.addAttribute("email", email);
            model.addAttribute("roleValue", role);
            model.addAttribute("creationRequest", request);
            return "enterVerifyOTP";
        }

        RegisterOTPVerify rv = new RegisterOTPVerify();
        try {
            rv = registerOTPVerifyRepository.findByOTPAndEmail(otp, email).orElseThrow(() -> new RuntimeException("This OTP is not for this email"));
        } catch (RuntimeException e) {
            model.addAttribute("otpMsg", e.getMessage());
            return "login";
        }


        if (rv.getExpirationTime().before(Date.from(Instant.now()))) {
            model.addAttribute("otpMsg", "The OTP has expired");
            registerOTPVerifyRepository.deleteById(rv.getRvid());
            return "login";
        }

        request.setActive(true);
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
