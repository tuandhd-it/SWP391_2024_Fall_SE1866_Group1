package project.dental_clinic_management.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.dental_clinic_management.entity.TimeTracking;
import project.dental_clinic_management.repository.EmployeeRepository;
import project.dental_clinic_management.repository.TimeTrackingRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class TimeTrackingService {

    private final TimeTrackingRepository timeTrackingRepository;
    private final EmployeeRepository employeeRepository;

    public TimeTrackingService(TimeTrackingRepository timeTrackingRepository, EmployeeRepository employeeRepository) {
        this.timeTrackingRepository = timeTrackingRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<TimeTracking> findByDate(LocalDate date) {
        return timeTrackingRepository.findAllByCheckInOrCheckOut(date);
    }
    public List<TimeTracking> findByEmpId(int emp_id) {
        return timeTrackingRepository.findAllByEmployeeId(emp_id);
    }
    public void checkIn(int employeeId) {
        List<TimeTracking> existingRecords = timeTrackingRepository.findAllByEmployeeIdAndCheckInDate(employeeId, LocalDate.now());

        if (!existingRecords.isEmpty()) {
            throw new IllegalStateException("Bạn đã check in hôm nay rồi."); // Hoặc xử lý thông báo khác
        }
        TimeTracking timeTracking = new TimeTracking();
        timeTracking.setCheckIn(LocalDateTime.now());
        // Giả sử có cách để lấy employee từ employeeId
        timeTracking.setEmployee(employeeRepository.findByEmp_id(employeeId));
        timeTrackingRepository.save(timeTracking);
    }

    public void checkOut(int employeeId) {
        Pageable pageable = PageRequest.of(0, 1);
        List<TimeTracking> latestRecords = timeTrackingRepository.findLatestByEmployeeId(employeeId, pageable);
        if (latestRecords != null) {
            TimeTracking timeTracking = latestRecords.get(0);
            timeTracking.setCheckOut(LocalDateTime.now());
            timeTrackingRepository.save(timeTracking);
        }
    }

    public List<TimeTracking> getMonthlyRecords(int month, int year,int employeeId) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.plusMonths(1).minusDays(1);
        return timeTrackingRepository.findAllByCheckInBetween(start, end, employeeId);
    }
}
