package project.dental_clinic_management.controller;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.dental_clinic_management.dto.request.ExamRegistrationRequest;
import project.dental_clinic_management.dto.request.ReceptionistCreationRequest;
import project.dental_clinic_management.dto.request.ViewExamRegistrationRequest;
import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.RegisterExamination;
import project.dental_clinic_management.entity.Role;
import project.dental_clinic_management.repository.RoleRepository;
import project.dental_clinic_management.service.CustomUserDetailService;
import project.dental_clinic_management.service.EmailService;
import project.dental_clinic_management.service.FileStorageService;
import project.dental_clinic_management.service.ReceptionistService;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GeneralController {

    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    private ReceptionistService receptionistService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;

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
        registerRoles.add(roleRepository.findById(3));
        registerRoles.add(roleRepository.findById(4));
        registerRoles.add(roleRepository.findById(5));
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
    public String homePage(Model model, @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("successMsg") String successMsg) {

        if (successMsg != null) {
            model.addAttribute("successMsg", successMsg);
        }
        if (userDetails != null) {

            model.addAttribute("loginSuccess", userDetails.getUsername());
            Employee employee = receptionistService.findByUsername(userDetails.getUsername());
            model.addAttribute("employee", employee);
        }
        return "LandingPage";
    }


    @GetMapping("/changePass")
    public String changePass(Model model, @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("error") String messageChange) {
        String username = userDetails.getUsername();
        Employee employee = receptionistService.findByUsername(username);
        model.addAttribute("employee", employee);
        if (messageChange != null && !messageChange.isEmpty()) {
            model.addAttribute("error", messageChange);
        }
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
                redirectAttributes.addFlashAttribute("error", "Hãy nhập đúng mật khẩu cũ");
                return "redirect:/changePass"; // Quay lại trang đổi mật khẩu nếu sai
            }
            if (!newPassword.equals(confirmNewPassword)) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu không trùng khớp");
                return "redirect:/changePass";
            }

            employee.setPassword(passwordEncoder.encode(newPassword));
            customUserDetailService.saveEmployee(employee);

            redirectAttributes.addFlashAttribute("messageChange", "Thay đổi mật khẩu thành công");
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "redirect:/changePass";
        }
        return "redirect:/profile";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("messageChange") String messageChange) {
        String username = userDetails.getUsername();
        Employee employee = customUserDetailService.findByUsername(username);
        model.addAttribute("employee", employee);
        if (messageChange != null && !messageChange.isEmpty()) {
            model.addAttribute("messageChange", messageChange);
        }
        model.addAttribute("editMode", false);
        return "/user/profile";
    }

    @PostMapping("/profile/update")
    @Transactional
    public String updateProfile(@Valid @ModelAttribute("employee") Employee employee,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                @AuthenticationPrincipal UserDetails userDetails,
                                Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("editMode", true);
            return "/user/profile";
        }

        try {
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
                existingEmployee.setCertification(employee.getCertification());
                existingEmployee.setSpecification(employee.getSpecification());
                existingEmployee.setSalary(employee.getSalary());
                existingEmployee.setDescription(employee.getDescription());
                customUserDetailService.saveEmployee(existingEmployee);
                redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin thành công!!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật thông tin: " + e.getMessage());
        }

        return "redirect:/profile";
    }

    @PostMapping("/profile/avatar")
    public String updateAvatar(@RequestParam("avatar") MultipartFile avatar, RedirectAttributes redirectAttributes, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String username = userDetails.getUsername();
            Employee employee = customUserDetailService.findByUsername(username);

            // Save the avatar file
            String avatarUrl = fileStorageService.saveImage(avatar);
            employee.setImg(avatarUrl);
            customUserDetailService.saveEmployee(employee);

            redirectAttributes.addFlashAttribute("message", "Ảnh đại diện đã được cập nhật thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi cập nhật ảnh đại diện: " + e.getMessage());
        }
        return "redirect:/profile";
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    //Đăng ký khám online
    @GetMapping("/guestExamRegistration")
    public String guestExamRegistration(Model model) {
        ExamRegistrationRequest request = new ExamRegistrationRequest();
        List<Branch> branchList = receptionistService.findAllBranches();
        model.addAttribute("branchList", branchList);
        model.addAttribute("request", request);
        List<Employee> doctors = receptionistService.findAllDoctorShift();
        model.addAttribute("doctors", doctors);
        return "/employee/guestExamRegistration";
    }

    //Đăng ký khám online thành công
    @PostMapping("/guestExamRegistration")
    public String guestExamRegistrationSubmit(@ModelAttribute("request") ExamRegistrationRequest request, Model model, RedirectAttributes redirectAttributes, @RequestParam("choosenDoctor") String empId) {
        request.setEmployeeId(empId);
        receptionistService.createExamRegistration(request);
        redirectAttributes.addFlashAttribute("successMsg", "Đăng ký khám thành công");
        //Gửi mail ở đây
        return "redirect:/homePage";
    }

    @PostMapping("/chooseDoctor")
    public String chooseDoctor(@ModelAttribute ExamRegistrationRequest request, Model model) {
        LocalDate choosenDate = request.getExamRegisterDate();
        String branchName = request.getBranchName();
        String shiftString = request.getShift();
        List<Employee> employeeList = receptionistService.findDoctorShiftForGuest(choosenDate, branchName, shiftString);
        model.addAttribute("request", request);
        model.addAttribute("employeeList", employeeList);
        return "/employee/chooseDoctor";
    }

    //Lay thong tin nhan vien trong lich lam viec
    @GetMapping("/getDetails")
    @ResponseBody
    @JsonIgnore
    public ViewExamRegistrationRequest getDetails(@RequestParam("examId") Long examId) {
        // Kiểm tra xem examId có tồn tại trong hệ thống không
        RegisterExamination registerExamination = receptionistService.findExamRegistrationByRegId(examId.toString());
        if (registerExamination == null) {
            throw new RuntimeException("Exam not found with id: " + examId);
        }
        return ViewExamRegistrationRequest.builder()
                .examId(examId)
                .firstName(registerExamination.getFirstName())
                .lastName(registerExamination.getLastName())
                .email(registerExamination.getEmail())
                .phone(registerExamination.getPhone())
                .reason(registerExamination.getReason())
                .dob(registerExamination.getDob())
                .gender(registerExamination.getGender())
                .examRegisterDate(registerExamination.getExamRegisterDate())
                .note(registerExamination.getNote())
                .branchName(registerExamination.getBranch().getBranchName())
                .doctorName(registerExamination.getEmployee().getFirst_name() + " " + registerExamination.getEmployee().getLast_name())
                .build();
    }


}
