package project.dental_clinic_management.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.TimeTracking;
import project.dental_clinic_management.repository.EmployeeRepository;
import project.dental_clinic_management.service.TimeTrackingService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/tracking")
public class TrackingController {
    private final EmployeeRepository employeeRepository;
    private final TimeTrackingService timeTrackingService;

    public TrackingController(EmployeeRepository employeeRepository, TimeTrackingService timeTrackingService) {
        this.employeeRepository = employeeRepository;
        this.timeTrackingService = timeTrackingService;
    }
    String mess = null;
    // Check-in action
    @PostMapping("/checkin")
    public String checkIn(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Employee employee = employeeRepository.findByEmail(userDetails.getUsername());
        try {
            timeTrackingService.checkIn(employee.getEmp_id());
            mess=  "Check in thành công!";
        } catch (IllegalStateException e) {
            mess = e.getMessage();
        }
        return "redirect:/tracking/attendance"; // Redirect to the attendance page
    }

    // Check-out action
    @PostMapping("/checkout")
    public String checkOut(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Employee employee = employeeRepository.findByEmail(userDetails.getUsername());
        timeTrackingService.checkOut(employee.getEmp_id());
        mess = "Check-out successful!";
        return "redirect:/tracking/attendance"; // Redirect to the attendance page
    }

    // View monthly records
    @GetMapping("/attendance")
    public String viewMonthlyRecords(@RequestParam(value = "month", required = false) Integer month,
                                     @RequestParam(value = "year", required = false) Integer year,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     Model model) {
        // Get the current month and year if not provided
        if (month == null || year == null) {
            LocalDateTime now = LocalDateTime.now();
            month = now.getMonthValue();
            year = now.getYear();
        }
        Employee employee = employeeRepository.findByEmail(userDetails.getUsername());
        List<TimeTracking> attendanceRecords = timeTrackingService.getMonthlyRecords(month, year,employee.getEmp_id());
        model.addAttribute("attendances", attendanceRecords);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        model.addAttribute("message", mess);
        mess = null;
        return "employee/tracking";
    }

    // Get attendance records within a date range
    @GetMapping("/attendanceByDate")
    public String getAttendanceByDate(@RequestParam(value = "startDate", required = false) String startDate,
                                      @RequestParam(value = "endDate", required = false) String endDate,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      Model model) {

        List<TimeTracking> attendanceRecords;

        if (startDate != null && endDate != null) {
            attendanceRecords = timeTrackingService.getRecordsBetweenDates(startDate, endDate);
        } else if (startDate != null) {
            attendanceRecords = timeTrackingService.getRecordsFromStartDate(startDate);
        } else if (endDate != null) {
            attendanceRecords = timeTrackingService.getRecordsUntilEndDate(endDate);
        } else {
            attendanceRecords = timeTrackingService.getAllRecords();
        }

        model.addAttribute("attendances", attendanceRecords);
        return "employee/tracking";
    }
}
