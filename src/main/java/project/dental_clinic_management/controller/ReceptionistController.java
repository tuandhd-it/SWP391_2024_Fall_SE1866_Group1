package project.dental_clinic_management.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.dental_clinic_management.dto.request.ExamRegistrationRequest;
import project.dental_clinic_management.dto.request.ViewExamRegistrationRequest;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.RegisterExamination;
import project.dental_clinic_management.service.ReceptionistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequestMapping("/recep")
public class ReceptionistController {

    @Autowired
    private ReceptionistService receptionistService;

    @GetMapping("/examRegistration")
    public String examRegistration(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Employee receptionist = receptionistService.findByUsername(username);
        ExamRegistrationRequest request = new ExamRegistrationRequest();
        String branchName = receptionist.getBranch().getBranchName();
        request.setBranchName(branchName);
        model.addAttribute("request", request);
        List<Employee> doctors = receptionistService.findAllBranchDoctor(receptionist);
        model.addAttribute("doctors", doctors);
        return "/employee/examRegistration";
    }

    @PostMapping("/examRegistrationSubmit")
    public String examRegistrationSubmit(@ModelAttribute ExamRegistrationRequest request, Model model, RedirectAttributes redirectAttributes) {
        receptionistService.createExamRegistration(request);
        redirectAttributes.addFlashAttribute("successMsg", "Đăng ký khám thành công");
        return "redirect:/recep/viewRegistration";
    }

    @GetMapping("/viewRegistration")
    public String viewRegistration(Model model, @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("successMsg") String successMsg) {
        String username = userDetails.getUsername();
        Employee receptionist = receptionistService.findByUsername(username);
        List<ViewExamRegistrationRequest> list = receptionistService.findAllBranchExam(receptionist);

        // Thêm danh sách vào model để hiển thị trong view
        model.addAttribute("examList", list);
        model.addAttribute("keyword", "");

        // Thêm thông báo thành công (nếu có) vào model
        if (successMsg != null) {
            model.addAttribute("successMsg", successMsg);
        }

        return "/employee/viewListExamRegistration";
    }

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

    @GetMapping("/search")
    public String search(Model model, @RequestParam("keyword") String keyword) {
        List<ViewExamRegistrationRequest> requestList = receptionistService.searchAllExamRegistration(keyword);
        model.addAttribute("examList", requestList);
        model.addAttribute("keyword", keyword);
        return "/employee/viewListExamRegistration";
    }

}
