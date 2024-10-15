package project.dental_clinic_management.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.dental_clinic_management.dto.request.ExamRegistrationRequest;
import project.dental_clinic_management.dto.request.ViewExamRegistrationRequest;
import project.dental_clinic_management.entity.Branch;
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
        model.addAttribute("request", request);
        List<Employee> doctors = receptionistService.findAllBranchDoctor(receptionist);
        model.addAttribute("doctors", doctors);
        List<Branch> branches = receptionistService.findAllBranches();
        model.addAttribute("branches", branches);
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
        model.addAttribute("keyword", "a");
        model.addAttribute("sortKey", "a");

        // Thêm thông báo thành công (nếu có) vào model
        if (successMsg != null) {
            model.addAttribute("successMsg", successMsg);
        }

        return "/employee/viewListExamRegistration";
    }

    @GetMapping("/getDetails")
    public String getDetails(@RequestParam("id") Long listId, Model model) {
        // Lấy thông tin chi tiết theo listId từ database hoặc service
        RegisterExamination registerExamination = receptionistService.findExamRegistrationByRegId(listId.toString());
        model.addAttribute("registerExamination", registerExamination);

        // Trả về dữ liệu HTML cho phần chi tiết
        return "/employee/ExamRegistrationDetails";
    }

}
