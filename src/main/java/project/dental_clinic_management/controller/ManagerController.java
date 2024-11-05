package project.dental_clinic_management.controller;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.dental_clinic_management.dto.request.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.dental_clinic_management.dto.request.ScheduleCreationRequest;
import project.dental_clinic_management.dto.request.ViewDoctorInfoRequest;
import project.dental_clinic_management.dto.request.ViewDoctorSpecCert;
import project.dental_clinic_management.dto.request.ViewExamRegistrationRequest;
import project.dental_clinic_management.dto.response.BranchEmployeeResponse;
import project.dental_clinic_management.dto.response.ScheduleEmployeeInfoResponse;
import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.repository.MedicineRepository;
import project.dental_clinic_management.service.ManagerService;
import project.dental_clinic_management.service.ReceptionistService;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    private final ReceptionistService receptionistService;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    public ManagerController(ManagerService managerService, ReceptionistService receptionistService) {
        this.managerService = managerService;
        this.receptionistService = receptionistService;
    }

    @GetMapping("/scheduleList")
    public String getSchedule(Model model, @AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("successMsg") String successMsg,
                              @ModelAttribute("scheduleDate") String scheduleDate,
                              @ModelAttribute("shift") String shiftString,
                              @ModelAttribute("employee") String employeeId,
                              @ModelAttribute("falseMsg") String falseMsg) {
        String currentUserRole = managerService.findByUsername(userDetails.getUsername()).getRole().getRoleName();
        model.addAttribute("currentUserRole", currentUserRole);
        model.addAttribute("successMsg", successMsg);
        model.addAttribute("scheduleDate", scheduleDate);
        model.addAttribute("shift", shiftString);
        model.addAttribute("employee", employeeId);
        model.addAttribute("falseMsg", falseMsg);
        return "/employee/scheduleList"; // Tên của file HTML (schedule.html)
    }

    @GetMapping("/scheduleData")
    @ResponseBody
    public List<ScheduleEmployeeInfoResponse> scheduleData(@AuthenticationPrincipal UserDetails userDetails) {
        List<ScheduleEmployeeInfoResponse> responseList = new ArrayList<>();
        List<Schedule> scheduleList = managerService.getAllSchedules();

        String currentUserRole = managerService.findByUsername(userDetails.getUsername()).getRole().getRoleName();
        for (Schedule schedule : scheduleList) {
            responseList.add(ScheduleEmployeeInfoResponse.builder()
                    .scheduleId(schedule.getScheduleId())
                    .shift(schedule.isShift())
                    .date(schedule.getDate())
                    .currentUserRole(currentUserRole)
                    .employeeId(schedule.getEmployee().getEmp_id())
                    .employeeName(schedule.getEmployee().getFirst_name() + " " + schedule.getEmployee().getLast_name())
                    .employeeRole(schedule.getEmployee().getRole().getRoleName())
                    .build());
        }
        return responseList;
    }

    @GetMapping("/getDetails")
    @ResponseBody
    @JsonIgnore
    public ViewDoctorInfoRequest getDetails(@RequestParam("empId") int empId) {
        // Kiểm tra xem empId có tồn tại trong hệ thống không
        Employee employee = managerService.getEmployeeById(empId);


        if (employee == null) {
            throw new RuntimeException("Doctor not found with id: " + empId);
        }
        return ViewDoctorInfoRequest.builder()
                .emp_id(empId)
                .doctorName(employee.getFirst_name() + " " + employee.getLast_name())
                .email(employee.getEmail())
                .phone(employee.getPhone())
                .dob(employee.getDob())
                .roleName(employee.getRole().getRoleName())
                .gender(employee.getGender())
                .img(employee.getImg())
                .build();
    }

    @PostMapping("/addSchedule")
    public String addSchedule(@RequestParam("scheduleDate") String scheduleDate,
                              @RequestParam("shift") String shiftString,
                              @RequestParam("employee") String employeeId,
                              RedirectAttributes redirectAttributes) {

        // Chuyển đổi chuỗi 'YYYY-MM-DD' thành LocalDate
        LocalDate date = LocalDate.parse(scheduleDate);
        boolean shift = !shiftString.equals("morning");
        ScheduleCreationRequest scheduleCreationRequest = ScheduleCreationRequest.builder()
                .employeeId(Integer.parseInt(employeeId))
                .date(date)
                .shift(shift)
                .build();
        if (!managerService.checkExistedSchedule(scheduleCreationRequest)) {
            managerService.createSchedule(scheduleCreationRequest);
            redirectAttributes.addFlashAttribute("successMsg", "Thêm lịch thành công");
        } else {
            redirectAttributes.addFlashAttribute("falseMsg", "Nhân viên đã có ca làm trong ca đã chọn");
            redirectAttributes.addFlashAttribute("scheduleDate", scheduleDate);
            redirectAttributes.addFlashAttribute("shift", shiftString);
            redirectAttributes.addFlashAttribute("employee", employeeId);
        }
        return "redirect:/manager/scheduleList";
    }

    @GetMapping("/employees")
    @ResponseBody
    public List<BranchEmployeeResponse> branchEmployees(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<BranchEmployeeResponse> responseList = new ArrayList<>();
        Employee manager = managerService.findByUsername(userDetails.getUsername());
        if (manager == null) {
            throw new RuntimeException("Employee not found with id: " + userDetails.getUsername());
        } else {
            List<Employee> employeeList = managerService.findAllBranchEmployee(manager);
            for (Employee employee : employeeList) {
                responseList.add(BranchEmployeeResponse.builder()
                        .employeeId(employee.getEmp_id())
                        .employeeFullName(employee.getFirst_name() + " " + employee.getLast_name())
                        .build());
            }
            return responseList;
        }
    }

    @GetMapping("/viewRegistration")
    public String viewRegistration(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        Employee receptionist = receptionistService.findByUsername(username);
        List<ViewExamRegistrationRequest> list = receptionistService.findAllBranchExam(receptionist);

        // Thêm danh sách vào model để hiển thị trong view
        model.addAttribute("examList", list);
        model.addAttribute("keyword", "");

        return "/employee/viewListExamRegistration";
    }

    @PostMapping("/acceptExamination")
    public String acceptExamination(@RequestParam List<Long> exam_id, Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam("action") String action) {
        if (action.equalsIgnoreCase("accept")) {
            managerService.acceptExamination(exam_id);
            model.addAttribute("acceptMsg", "Phê duyệt thành công!");
            //Gửi mail ở đây
        } else {
            managerService.rejectExamination(exam_id);
            model.addAttribute("rejectMsg", "Từ chối đơn khám thành công!");
            //Gửi mail ở đây
        }

        String username = userDetails.getUsername();
        Employee manager = receptionistService.findByUsername(username);
        List<ViewExamRegistrationRequest> list = receptionistService.findAllBranchExam(manager);

        model.addAttribute("examList", list);
        return "/employee/viewListExamRegistration";
    }

    @GetMapping("/getExamDetails")
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

    @GetMapping("/getDoctorDetails")
    @ResponseBody
    @JsonIgnore
    public ViewDoctorSpecCert getDoctorDetails(@RequestParam("empId") int empId) {
        // Kiểm tra xem examId có tồn tại trong hệ thống không
        Employee employee = managerService.getEmployeeById(empId);
        if (employee == null) {
            throw new RuntimeException("Employee not found with id: " + empId);
        }
        return ViewDoctorSpecCert.builder()
                .spec(employee.getSpecification())
                .cert(employee.getCertification())
                .doctorName(employee.getFirst_name() + " " + employee.getLast_name())
                .img(employee.getImg())
                .build();
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam("keyword") String keyword) {
        List<ViewExamRegistrationRequest> requestList = receptionistService.searchAllExamRegistration(keyword);
        model.addAttribute("examList", requestList);
        model.addAttribute("keyword", keyword);
        return "/employee/viewListExamRegistration";
    }

    //Xoá lịch làm việc
    @PostMapping("/deleteSchedule")
    public String deleteSchedule(@RequestParam("dateValue") String dateValue, @RequestParam("empId") int empId, @RequestParam("shift") String shiftString, RedirectAttributes redirectAttributes) {
        LocalDate date = LocalDate.parse(dateValue);
        boolean shift = !shiftString.equals("morning");
        managerService.deleteScheduleByEmpIdAndScheduleId(empId, date, shift);
        redirectAttributes.addFlashAttribute("falseMsg", "Xoá lịch làm việc thành công!");
        return "redirect:/manager/scheduleList";
        }

    //Quản lý thuốc

    @GetMapping("/manageMedicine")
    public String getAllMedicines(Model model) {
        List<Medicine> list = managerService.getAllMedicines();
        model.addAttribute("medicines", list);
        return "/medicine/manageMedicine";
    }
    //Thêm thuốc
    @GetMapping("/addMedicine")
    public String showAddMedicineForm(Model model) {
        model.addAttribute("importMedicine", new MedicineImportRequest());
        return "medicine/manageMedicine";
    }
    @PostMapping("/medicineImport")
    public String importMedicine(@ModelAttribute("importMedicine") MedicineImportRequest request, RedirectAttributes redirectAttributes) {
        try {
            managerService.importMedicine(request); // Gọi service để thêm thuốc
            redirectAttributes.addFlashAttribute("successAdding", "Nhập thuốc thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorAdding", "Lỗi khi nhập thuốc: " + e.getMessage());
        }
        return "redirect:/manager/manageMedicine";
    }

    @GetMapping("/medicines")
    public String listMedicines(@RequestParam(defaultValue = "0") int page, Model model) {
        int pageSize = 6; // Set the page size to 6
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Medicine> medicinePage = medicineRepository.findAll(pageable);
        model.addAttribute("medicinePage", medicinePage);
        return "medicine/manageMedicine";
    }
    //update medicine
    @PostMapping("/updateMedicine")
    public String updateMedicine(
            @ModelAttribute("medicine") Medicine updatedMedicine,
            RedirectAttributes redirectAttributes) {

        try {
            if (updatedMedicine.getRegNumber()>0) {
                managerService.updateMedicine(updatedMedicine);
                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thuốc thành công!");
            } else {
                throw new RuntimeException("Số đăng ký thuốc không hợp lệ!");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Cập nhật thất bại: " + e.getMessage());
        }

        return "redirect:/manager/manageMedicine";
    }
    @GetMapping("/searchMedicine")
    public String searchMedicine(@RequestParam("name") String name, Model model) {
        List<Medicine> medicines = managerService.searchMedicineByName(name);
        model.addAttribute("medicines", medicines);
        return "medicine/manageMedicine";
    }
    @GetMapping("/sortMedicine")
    public String sortMedicine(@RequestParam(name = "sortPrice") String sortPrice,
                               Model model) {
        List<Medicine> medicines;

        // Kiểm tra người dùng muốn lọc theo giá tăng hoặc giảm
        if ("asc".equals(sortPrice)) {
            medicines = managerService.findAllByOrderByPriceAsc();
        } else if ("desc".equals(sortPrice)) {
            medicines = managerService.findAllByOrderByPriceDesc();
        } else {
            // Nếu giá trị không hợp lệ, có thể trả về lỗi hoặc xử lý như mong muốn
            throw new IllegalArgumentException("Invalid sort option");
        }

        model.addAttribute("medicines", medicines);
        return "medicine/manageMedicine";
    }

    @GetMapping("/medicineHistory")
    public String getAllMedicineImports(Model model) {
        List<MedicineImport> medicineImports = managerService.getAllMedicineImports();
        model.addAttribute("medicineImports", medicineImports);
        return "medicine/medicineHistory";
    }
    @GetMapping("/manageEquipment")
    public String getAllEquipments(Model model) {
        List<Equipment> list = managerService.getAllEquipments();
        model.addAttribute("equipments", list);
        return "/equipment/manageEquipment";
    }
}

