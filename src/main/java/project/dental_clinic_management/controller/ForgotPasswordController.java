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

    //Chuyển đến trang nhập email
    @GetMapping("/enterEmail")
    public String enterEmail() {
        return "/auth/enterEmail";
    }

    //Xác thực OTP
    @PostMapping("/verifyOTP")
    public String verifyOTP(@RequestParam("otp") Integer otp, @RequestParam("email") String email, Model model) {
        Employee employee = employeeRepository.findByEmail(email);
        ForgotPassword checkRightOTP = forgotPasswordRepository.findByOtp(otp);
        model.addAttribute("email", email);

        //Kiểm tra xem OTP có đúng không
        if(checkRightOTP == null) {
            model.addAttribute("msg", "Wrong OTP");
            model.addAttribute("otp", otp);
            return "/auth/enterForgotOTP";
        }

        //Kiểm tra xem OTP đã hết hạn chưa
        ForgotPassword fp = forgotPasswordRepository.findByEmployeeAndOtp(employee, otp);
        if(fp.getExpirationTime().before(Date.from(Instant.now()))) {
            model.addAttribute("msg", "The OTP has expired");
            forgotPasswordRepository.deleteById(fp.getFpid());
            return "/auth/enterEmail";
        }
        model.addAttribute("msg", "OTP verified");
        forgotPasswordRepository.deleteById(fp.getFpid());
        return "/auth/newPassword";
    }

    //Xác nhận xem email đã được đăng ký trong hệ thống chưa
    @PostMapping("/verify")
    public String forgotPassword(@RequestParam("email") String email, Model model) {
        Employee employee = employeeRepository.findByEmail(email);

        if(employee == null) {
            model.addAttribute("emailError", "This email does not exist");
            return "/auth/enterEmail";
        }

        int otp = otpGenerator();

        //Tạo mã OTP cho quên mật khẩu với thời hạn là 70 giây
        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expirationTime(new Date(System.currentTimeMillis() + 70 * 1000))
                .employee(employee)
                .build();

        //Tạo 1 tin nhắn mail để gửi tới email user
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("Dear,\n\n" +
                        "We received a request to " +
                        " your account.\nTo proceed, please use the " +
                        "following One-Time Password (OTP):\n\nOTP Code: " + otp+"\n\nThis code is valid for the next 1 minutes. \n" +
                        "Please enter the code in the required field to complete your request.\n\nBest regards,\nDCMS Team :)")
                .subject("Your single-use code for DCMS")
                .build();

        emailService.sendSimpleMessage(mailBody);
        forgotPasswordRepository.save(fp);
        model.addAttribute("email", email);
        model.addAttribute("msg", "OTP has sent to email for verification");

        return "/auth/enterForgotOTP";
    }

    //Đổi mật khẩu
    @PostMapping("/changePassword")
    public String changePass(@RequestParam("password") String newPassword, @RequestParam("email") String email, Model model) {
        String passwordEncode = new BCryptPasswordEncoder().encode(newPassword);
        employeeRepository.updatePassword(email, passwordEncode);

        model.addAttribute("msgChangePass", "Password has been changed");
        return "/auth/login";
    }

    //Tạo random OTP có 6 chữ số
    private Integer otpGenerator() {
        Random random = new Random();
        return random.nextInt(100000, 999999);
    }
}
