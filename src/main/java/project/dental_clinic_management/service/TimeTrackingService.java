package project.dental_clinic_management.service;

import org.springframework.stereotype.Service;
import project.dental_clinic_management.entity.TimeTracking;
import project.dental_clinic_management.repository.TimeTrackingRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TimeTrackingService {

    private final TimeTrackingRepository timeTrackingRepository;

    public TimeTrackingService(TimeTrackingRepository timeTrackingRepository) {
        this.timeTrackingRepository = timeTrackingRepository;
    }

    public List<TimeTracking> findByDate(LocalDate date) {
        return timeTrackingRepository.findAllByCheckInOrCheckOut(date);
    }
    public List<TimeTracking> findByEmpId(int emp_id) {
        return timeTrackingRepository.findAllByEmployeeId(emp_id);
    }
}
