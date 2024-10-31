package project.dental_clinic_management.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.Schedule;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query("SELECT s FROM Schedule s WHERE s.employee.emp_id = ?1")
    public List<Schedule> findByEmpId(int id);

    @Query("SELECT s.employee FROM Schedule s WHERE s.date = ?2 AND s.shift = ?1")
    public List<Employee> findEmployeeByShift(boolean shift, LocalDate currentDate);

    @Modifying
    @Transactional
    @Query("DELETE FROM Schedule s WHERE s.date = ?1 AND s.employee.emp_id = ?2 AND s.shift = ?3")
    public void deleteEmpSchedule(LocalDate date, int empId, boolean shift);
}
