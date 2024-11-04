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
import project.dental_clinic_management.entity.Schedule;
import project.dental_clinic_management.entity.TimeTracking;
import project.dental_clinic_management.repository.EmployeeRepository;
import project.dental_clinic_management.repository.ScheduleRepository;
import project.dental_clinic_management.service.TimeTrackingService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/tracking")
public class TrackingController {
    private final EmployeeRepository employeeRepository;
    private final TimeTrackingService timeTrackingService;
    private final ScheduleRepository scheduleRepository;
    String mess = null;

    public TrackingController(EmployeeRepository employeeRepository, TimeTrackingService timeTrackingService, ScheduleRepository scheduleRepository) {
        this.employeeRepository = employeeRepository;
        this.timeTrackingService = timeTrackingService;
        this.scheduleRepository = scheduleRepository;
    }

    private boolean isValidateSchedule(Employee employee) {
        List<Schedule> schedules = scheduleRepository.findByEmpId(employee.getEmp_id());
        if (schedules.isEmpty()) {
            mess = "Bạn chưa có lịch làm việc!";
            return false;
        }

        if (schedules.getFirst().getDate().isAfter(LocalDate.now())) {
            mess = "Chưa đến ngày làm việc!";
            return false;
        }

        if (schedules.getLast().getDate().isBefore(LocalDate.now())) {
            mess = "Đã hết ngày làm việc!";
            return false;
        }

        if (schedules.stream().noneMatch(schedule -> schedule.getDate().isEqual(LocalDate.now()))) {
            mess = "Hôm nay không có lịch làm việc!";
            return false;
        }
        return true;
    }

    @PostMapping("/checkin")
    public String checkIn(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) String reason , Model model) {
        Employee employee = employeeRepository.findByEmail(userDetails.getUsername());
        if (!isValidateSchedule(employee)) {
            return "redirect:/tracking/attendance";
        }
        try {
            timeTrackingService.checkIn(employee.getEmp_id(), reason);
            mess = "Check in thành công!";
        } catch (IllegalStateException e) {
            mess = e.getMessage();
        }
        model.addAttribute("message", mess);
        return "employee/tracking";
    }

    @PostMapping("/checkout")
    public String checkOut(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(required = false) String reason , Model model) {
        Employee employee = employeeRepository.findByEmail(userDetails.getUsername());
        if (!isValidateSchedule(employee)) {
            return "redirect:/tracking/attendance";
        }
        try {
            timeTrackingService.checkOut(employee.getEmp_id(), reason);
            mess = "Check-out thành công!";
        } catch (IllegalStateException e) {
            mess = e.getMessage();
        }
        model.addAttribute("message", mess);
        return "employee/tracking";
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
        List<TimeTracking> attendanceRecords = timeTrackingService.getMonthlyRecords(month, year, employee.getEmp_id());
        Schedule schedule = scheduleRepository.findByEmpId(employee.getEmp_id()).stream().filter(s -> Objects.equals(s.getDate(), LocalDate.now())).findFirst().orElse(null);
        if (schedule == null) {
            mess = "Bạn chưa có lịch làm việc!";
        } else {
            model.addAttribute("shift", schedule.isShift());
        }
        model.addAttribute("attendances", attendanceRecords);
        model.addAttribute("selectedMonth", month);
        model.addAttribute("selectedYear", year);
        model.addAttribute("message", mess);

        mess = null;
        return "employee/tracking";
    }

    @GetMapping("/attendanceByDate")
    public String getAttendanceByDate(@RequestParam(value = "startDate", required = false) LocalDate startDate,
                                      @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                      @AuthenticationPrincipal UserDetails userDetails,
                                      Model model) {
        Employee employee = employeeRepository.findByEmail(userDetails.getUsername());
        if (!Objects.equals(employee.getRole().getRoleName(), "Admin")) {
            model.addAttribute("message", "Bạn không có quyền truy cập vào trang này!");
            return "redirect:/tracking/attendance";
        }
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
        return "timeTracking/managerTimeTracking";
    }
}