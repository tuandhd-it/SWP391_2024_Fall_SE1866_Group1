package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.Service;
import project.dental_clinic_management.entity.TimeTracking;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimeTrackingRepository extends JpaRepository<TimeTracking, Integer> {
    @Query("SELECT t FROM TimeTracking t WHERE DATE(t.checkIn) = :date OR DATE(t.checkOut) = :date")
    List<TimeTracking> findAllByCheckInOrCheckOut(LocalDate date);
    @Query("SELECT e FROM TimeTracking e WHERE e.employee.emp_id = ?1")
    List<TimeTracking> findAllByEmployeeId(int emp_id);
}
