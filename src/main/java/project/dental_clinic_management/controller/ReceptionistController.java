package project.dental_clinic_management.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.dental_clinic_management.dto.request.ExamRegistrationRequest;
import project.dental_clinic_management.dto.request.PatientCreationRequest;
import project.dental_clinic_management.dto.request.PatientWaitingRoomRequest;
import project.dental_clinic_management.dto.request.ViewExamRegistrationRequest;
import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.service.AdminService;
import project.dental_clinic_management.service.ReceptionistService;

import java.util.List;

@Controller
@RequestMapping("/recep")
public class ReceptionistController {

    @Autowired
    private ReceptionistService receptionistService;

    @Autowired
    private AdminService adminService;

    @GetMapping("/examRegistration")
    public String examRegistration(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Employee receptionist = receptionistService.findByUsername(username);
        ExamRegistrationRequest request = new ExamRegistrationRequest();
        String branchName = receptionist.getBranch().getBranchName();
        request.setBranchName(branchName);
        model.addAttribute("request", request);
        List<Employee> doctors = receptionistService.findAllDoctorShift();
        model.addAttribute("doctors", doctors);
        return "/employee/examRegistration";
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

    @GetMapping("/searchAcceptedExamination")
    public String searchAcceptedExamination(Model model, @RequestParam("keyword") String keyword, @RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
        Page<ViewExamRegistrationRequest> requestList = receptionistService.searchAllPageExamRegistrationAccepted(keyword, pageNo);
        model.addAttribute("examList", requestList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", requestList.getTotalPages());
        return "/employee/viewListExamRegistrationRecep";
    }

    //Hiển thị lịch làm việc cá nhân
    @GetMapping("/myScheduleList")
    public String myScheduleList(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
        String username = userDetails.getUsername();
        Employee employee = receptionistService.findByUsername(username);
        Page<Schedule> schedules = receptionistService.getPageSchedulesByEmployeeId(employee.getEmp_id(), pageNo);
        if (schedules == null || schedules.isEmpty()) {
            model.addAttribute("notFoundMsg", "This employee do not have any work schedules");
            model.addAttribute("totalPage", 0);
        } else {
            model.addAttribute("schedules", schedules);
            model.addAttribute("totalPage", schedules.getTotalPages());
        }
        model.addAttribute("currentPage", pageNo);
        return "/employee/myScheduleList";
    }

    //Thêm bệnh nhân vào phòng chờ từ đơn khám online
    @GetMapping("/viewListExaminationOnline")
    public String viewListExaminationOnline(Model model, @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("acceptMsg") String acceptMsg,
                                            @ModelAttribute("deleteMsg") String deleteMsg, @RequestParam(name = "pageNo", defaultValue = "1") int pageNo) {
        String username = userDetails.getUsername();
        Employee employee = receptionistService.findByUsername(username);
        Page<ViewExamRegistrationRequest> viewExamRegistrationRequestList = receptionistService.findAllPageBranchExamAccept(employee, pageNo);
        model.addAttribute("examList", viewExamRegistrationRequestList);
        model.addAttribute("acceptMsg", acceptMsg);
        model.addAttribute("rejectMsg", deleteMsg);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", viewExamRegistrationRequestList.getTotalPages());

        return "/employee/viewListExamRegistrationRecep";
    }

    @GetMapping("/addExamToWaitingRoom/{examId}")
    public String addExamToWaitingRoom(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long examId, RedirectAttributes redirectAttributes) {
        RegisterExamination registerExamination = receptionistService.findExamRegistrationByRegId(examId.toString());
        String username = userDetails.getUsername();
        Employee employee = receptionistService.findByUsername(username);
        Patient existedPatient = receptionistService.findPatientByPhone(registerExamination.getPhone());
        PatientWaitingRoomRequest request;
        if (existedPatient != null) {
            request = PatientWaitingRoomRequest.builder()
                    .patient(existedPatient)
                    .isBooked(true)
                    .isUrgency(false)
                    .waitingRoom(adminService.findWaitingRoomByBranchId(employee.getBranch().getBran_id()))
                    .note(registerExamination.getNote())
                    .build();

        } else {
            PatientCreationRequest creationRequest = PatientCreationRequest.builder()
                    .firstName(registerExamination.getFirstName())
                    .lastName(registerExamination.getLastName())
                    .gender(registerExamination.getGender())
                    .dob(registerExamination.getDob())
                    .phone(registerExamination.getPhone())
                    .email(registerExamination.getEmail())
                    .build();

            adminService.createPatient(creationRequest);

            Patient patient = receptionistService.findPatientByPhone(creationRequest.getPhone());

            request = PatientWaitingRoomRequest.builder()
                    .patient(patient)
                    .isBooked(true)
                    .isUrgency(false)
                    .waitingRoom(adminService.findWaitingRoomByBranchId(employee.getBranch().getBran_id()))
                    .note(registerExamination.getNote())
                    .build();
        }
        adminService.addPatientWaitingRoom(request);
        receptionistService.updateInWaitingRoom(registerExamination);

        redirectAttributes.addFlashAttribute("acceptMsg", "Chuyển bệnh nhân vào phòng chờ thành công");
        return "redirect:/recep/viewListExaminationOnline";
    }

    @GetMapping("/deleteExam/{examId}")
    public String deleteExam(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable Long examId, RedirectAttributes redirectAttributes) {
        RegisterExamination registerExamination = receptionistService.findExamRegistrationByRegId(examId.toString());
        if(registerExamination != null) {
            receptionistService.deleteExam(registerExamination);
            redirectAttributes.addFlashAttribute("deleteMsg", "Xoá đơn khám thành công!");
        }
        return "redirect:/recep/viewListExaminationOnline";
    }

    /**
     * Get data and bring it to page
     * @param userDetails
     * @param page
     * @param model
     * @return path to page
     */
    @GetMapping("/patientList")
    public String patientList(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "searchQuery", required = false) String searchQuery,
                              Model model) {

        String username = userDetails.getUsername();
        Employee receptionist = adminService.findByUsername(username);

        WaitingRoom waitingRoom = adminService.findWaitingRoomByBranchId(receptionist.getBranch().getBran_id());
        int waitingRoomId = waitingRoom.getWaitingRoomID();

        Page<PatientWaitingRoom> patientWaitingRequests;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            // Search for patients by name in the waiting room
            patientWaitingRequests = receptionistService.searchPatientsInWaitingRoomByNameStatusDone(page, waitingRoomId, searchQuery);
            model.addAttribute("searchQuery", searchQuery);
        } else {
            // Retrieve all patients in the waiting room with pagination
            patientWaitingRequests = receptionistService.getAllPatientWaitingRequestsInRoomIsDone(page, waitingRoomId);
        }

        // Add attributes to the model for view rendering
        model.addAttribute("listPatient", patientWaitingRequests.getContent());
        model.addAttribute("waitingRoomId", waitingRoomId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", patientWaitingRequests.getTotalPages());
        model.addAttribute("hasPrevious", patientWaitingRequests.hasPrevious());
        model.addAttribute("hasNext", patientWaitingRequests.hasNext());

        return "/employee/recepListPatient"; // Update this path as necessary for your project structure
    }

}
