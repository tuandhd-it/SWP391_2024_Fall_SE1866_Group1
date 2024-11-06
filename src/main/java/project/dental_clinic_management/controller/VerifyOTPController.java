package project.dental_clinic_management.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.dental_clinic_management.dto.MailBody;
import project.dental_clinic_management.dto.request.ReceptionistCreationRequest;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.RegisterOTPVerify;
import project.dental_clinic_management.entity.Role;
import project.dental_clinic_management.repository.RegisterOTPVerifyRepository;
import project.dental_clinic_management.service.EmailService;
import project.dental_clinic_management.service.ReceptionistService;

import java.time.Instant;
import java.util.*;

@Controller
@RequestMapping("/verifyEmail")

//Dùng @SessionAttributes để lưu creation request qua các trang liên quan
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

    //Xác thực email
    @PostMapping("/verify")
    public String verifyEmail(@RequestParam("email") String email, @RequestParam("roleValue") String role, Model model, @ModelAttribute @Valid ReceptionistCreationRequest request, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            bindingResult.getFieldErrors().forEach(
                    error -> errors.put(error.getField(), error.getDefaultMessage())
            );

            StringBuilder errorMsg = new StringBuilder();
            errorMsg.append("Tạo mới tài khoản thất bại, lí do:\n");
            for (String key : errors.keySet()) {
                errorMsg.append(errors.get(key)).append("\n");
            }
            redirectAttributes.addFlashAttribute("errors", errorMsg);
            return "redirect:/register";
        }
        String existed = receptionistService.checkExistedEmployee(request.getEmail(), request.getPhone());
        if (existed != null) {
            model.addAttribute("existed", existed);
            return "/auth/login";
        }
        if (role == null || role.isEmpty()) {
            redirectAttributes.addFlashAttribute("inputInformation", "You must input your information first!");
            return "redirect:/register";
        }

        model.addAttribute("creationRequest", request);
        model.addAttribute("roleValue", role);

        int otp = otpGenerator();

        //Tạo 1 OTP mới với email và thời gian hết hạn là 70 giây
        RegisterOTPVerify rv = RegisterOTPVerify.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .email(email)
                .build();

        //Tạo 1 tin nhắn mail để gửi tới email user
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("Dear,\n\n" +
                        "We received a request to verify your account.\nTo proceed, please use the " +
                        "following One-Time Password (OTP):\n\nOTP Code: " + otp + "\n\nThis code is valid for the next 1 minutes. \n" +
                        "Please enter the code in the required field to complete your request.\n\nBest regards,\nDCMS Team :)")
                .subject("Your single-use code for DCMS")
                .build();

        emailService.sendSimpleMessage(mailBody);
        RegisterOTPVerify registerOTPVerify = registerOTPVerifyRepository.findByEmail(email);
        if (registerOTPVerify != null) {
            registerOTPVerifyRepository.delete(registerOTPVerify);
        }
        registerOTPVerifyRepository.save(rv);


        model.addAttribute("email", email);
        model.addAttribute("msg", "OTP has sent to email for verification");

        return "/auth/enterVerifyOTP";
    }

    //Xác thực OTP
    @PostMapping("/verifyOTP")
    public String verifyOTP(@RequestParam("otp") Integer otp, @RequestParam("email") String email, @RequestParam("roleValue") String role, @ModelAttribute("creationRequest") ReceptionistCreationRequest request, Model model, RedirectAttributes redirectAttributes) {
        Employee employee = receptionistService.findByEmail(email);

        //Kiểm tra xem OTP có đúng không
        RegisterOTPVerify checkRightOTP = registerOTPVerifyRepository.findByOtp(otp);
        if (checkRightOTP == null) {
            model.addAttribute("otpMsg", "Wrong OTP");
            model.addAttribute("otp", otp);
            model.addAttribute("email", email);
            model.addAttribute("roleValue", role);
            model.addAttribute("creationRequest", request);
            return "/auth/enterVerifyOTP";
        }

        if (role == null || role.isEmpty()) {
            redirectAttributes.addFlashAttribute("inputInformation", "You must input your information first!");
            return "redirect:/register";
        }

        List<RegisterOTPVerify> registerVerify;

        //Kiểm tra xem OTP có đúng là của email đó không
        try {
            registerVerify = registerOTPVerifyRepository.findByOTPAndEmail(otp, email);
            if (registerVerify.isEmpty()) {
                throw new RuntimeException("This OTP is not for this email!");
            }
        } catch (RuntimeException e) {
            model.addAttribute("otpMsg", e.getMessage());
            return "/auth/login";
        }

        //Kiểm tra xem OTP đã hết hạn chưa
        if (registerVerify.getFirst().getExpirationTime().before(Date.from(Instant.now()))) {
            model.addAttribute("otpMsg", "The OTP has expired");
            registerOTPVerifyRepository.deleteById(registerVerify.getFirst().getRvid());
            model.addAttribute("email", email);
            return "/auth/enterVerifyOTP";
        }

        //Mặc định là tài khoản cần được xét duyệt để đăng nhập vào hệ thống
        request.setActive(false);
        request.setSalary(0);
        Role choosenRole = receptionistService.findByRoleName(role);
        request.setRole(choosenRole);
        receptionistService.createReceptionist(request);
        model.addAttribute("message", "Registered successfully");
        model.addAttribute("otpMsg", "OTP verified");
        registerOTPVerifyRepository.deleteById(registerVerify.getFirst().getRvid());

        // Xóa khỏi session sau khi đã sử dụng
        model.asMap().remove("creationRequest");
        return "/auth/login";
    }


    //Tạo random OTP có 6 chữ số
    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100000, 999999);
    }
}
