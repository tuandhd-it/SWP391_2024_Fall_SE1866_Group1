package project.dental_clinic_management.controller;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
import project.dental_clinic_management.dto.MailBody;
import project.dental_clinic_management.dto.request.ExamRegistrationRequest;
import project.dental_clinic_management.dto.request.ReceptionistCreationRequest;
import project.dental_clinic_management.dto.request.ViewExamRegistrationRequest;
import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.repository.*;
import project.dental_clinic_management.service.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

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
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private TimeTrackingRepository timeTrackingRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

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
        List<Service> services = serviceRepository.findAll();
        List<Employee> doctors = employeeRepository.findByRole(roleRepository.findById(3));
        model.addAttribute("services", services);
        model.addAttribute("doctors", doctors);
        return "LandingPage";
    }


    @GetMapping("/changePass")
    public String changePass(Model model, @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("error") String messageChange, @ModelAttribute("message") String message) {
        String username = userDetails.getUsername();
        Employee employee = receptionistService.findByUsername(username);
        model.addAttribute("employee", employee);
        if (messageChange != null && !messageChange.isEmpty()) {
            model.addAttribute("error", messageChange);
            model.addAttribute("message", message);
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
                return "redirect:/changePass";
            }
            if (!newPassword.equals(confirmNewPassword)) {
                redirectAttributes.addFlashAttribute("error", "Mật khẩu không trùng khớp");
                return "redirect:/changePass";
            }

            employee.setPassword(passwordEncoder.encode(newPassword));
            customUserDetailService.saveEmployee(employee);

            redirectAttributes.addFlashAttribute("message", "Thay đổi mật khẩu thành công");
            return "redirect:/changePass";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred: " + e.getMessage());
            return "redirect:/changePass";
        }
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
        //Tạo 1 tin nhắn mail để gửi tới email user
        MailBody mailBody = MailBody.builder()
                .to(request.getEmail())
                .text("Dear " + request.getFirstName() + " " + request.getLastName() + ",\n" +
                        "\n" +
                        "Thank you for submitting your examination registration request. We are currently reviewing your information to confirm your booking.\n" +
                        "\n" +
                        "Your registration is important to us, and we are committed to ensuring a smooth process. You will receive an update from us shortly with the status of your booking, or if there is any additional information needed to complete it.\n" +
                        "\n" +
                        "Thank you for your patience and trust in our services.\n" +
                        "\n" +
                        "Best regards,\n" +
                        "DCMS Team")
                .subject("Your Examination Registration is Under Review")
                .build();

        emailService.sendSimpleMessage(mailBody);
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



    @GetMapping("attendanceStatistic")
    public String attendanceStatistic(Model model) {
        List<Employee> employees = employeeRepository.findAll();
        List<TimeTracking> timeTrackings = timeTrackingRepository.findAll();
        List<Schedule> schedules = scheduleRepository.findByDate(LocalDate.now());
        int onTimeCount = 0;
        int lateCount = 0;
        int absentCount = 0;

        for (TimeTracking timeTracking : timeTrackings) {
            Employee employee = timeTracking.getEmployee();
            LocalTime checkInTime = timeTracking.getCheckIn().toLocalTime();
            Schedule schedule = schedules.stream().filter(s -> s.getEmployee().getEmp_id() == employee.getEmp_id()).findFirst().orElse(null);

            if (schedule != null) {
                if (!schedule.isShift()) {
                    if (checkInTime.isBefore(LocalTime.of(7, 15))) {
                        onTimeCount++;
                    } else {
                        lateCount++;
                    }
                } else {
                    if (checkInTime.isBefore(LocalTime.of(13, 15))) {
                        onTimeCount++;
                    } else {
                        lateCount++;
                    }
                }
            }
        }

        // Find absent employees
        List<Employee> absentEmployees = new ArrayList<>();
        for (Employee employee : employees) {
            boolean isAbsent = timeTrackings.stream().noneMatch(tt -> tt.getEmployee().getEmp_id() == employee.getEmp_id());
            if (isAbsent) {
                absentEmployees.add(employee);
                absentCount++;
            }
        }


        model.addAttribute("employees", employees);
        model.addAttribute("onTimeToday", onTimeCount);
        model.addAttribute("lateToday", lateCount);
        model.addAttribute("absentToday", absentCount);
        model.addAttribute("absentEmployees", absentEmployees);
        return "timeTracking/attendanceStatistics";
    }

    @GetMapping("/monthlyAttendanceStatistics")
    @ResponseBody
    public ResponseEntity<Map<String, Map<String, Integer>>> getMonthlyAttendanceStatistics() {
        List<TimeTracking> timeTrackings = timeTrackingRepository.findAll();
        Map<String, Map<String, Integer>> monthlyStatistics = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");

        for (TimeTracking timeTracking : timeTrackings) {
            LocalDate date = timeTracking.getCheckIn().toLocalDate();
            String month = date.format(formatter);

            monthlyStatistics.putIfAbsent(month, new HashMap<>());
            Map<String, Integer> stats = monthlyStatistics.get(month);

            Employee employee = timeTracking.getEmployee();
            LocalTime checkInTime = timeTracking.getCheckIn().toLocalTime();
            Schedule schedule = scheduleRepository.findByDateAndEmployee(date, employee);

            if (schedule != null) {
                if (!schedule.isShift()) {
                    if (checkInTime.isBefore(LocalTime.of(7, 15))) {
                        stats.put("onTime", stats.getOrDefault("onTime", 0) + 1);
                    } else {
                        stats.put("late", stats.getOrDefault("late", 0) + 1);
                    }
                } else {
                    if (checkInTime.isBefore(LocalTime.of(13, 15))) {
                        stats.put("onTime", stats.getOrDefault("onTime", 0) + 1);
                    } else {
                        stats.put("late", stats.getOrDefault("late", 0) + 1);
                    }
                }
            }
        }

        List<Employee> employees = employeeRepository.findAll();
        for (Employee employee : employees) {
            for (String month : monthlyStatistics.keySet()) {
                LocalDate firstDayOfMonth = LocalDate.parse("01 " + month, DateTimeFormatter.ofPattern("dd MMMM yyyy"));
                LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

                boolean isAbsent = timeTrackings.stream()
                        .noneMatch(tt -> tt.getEmployee().getEmp_id() == employee.getEmp_id() &&
                                !tt.getCheckIn().toLocalDate().isBefore(firstDayOfMonth) &&
                                !tt.getCheckIn().toLocalDate().isAfter(lastDayOfMonth));

                if (isAbsent) {
                    Map<String, Integer> stats = monthlyStatistics.get(month);
                    stats.put("absent", stats.getOrDefault("absent", 0) + 1);
                }
            }
        }

        return ResponseEntity.ok(monthlyStatistics);
    }
}
