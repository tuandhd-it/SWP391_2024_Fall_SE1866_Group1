package project.dental_clinic_management.controller;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.dental_clinic_management.dto.request.ScheduleCreationRequest;
import project.dental_clinic_management.dto.request.ViewDoctorInfoRequest;
import project.dental_clinic_management.dto.response.BranchEmployeeResponse;
import project.dental_clinic_management.dto.response.ScheduleEmployeeInfoResponse;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.Schedule;
import project.dental_clinic_management.service.ManagerService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
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
                            .empployeeRole(schedule.getEmployee().getRole().getRoleName())
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
        if(!managerService.checkExistedSchedule(scheduleCreationRequest)) {
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
}
