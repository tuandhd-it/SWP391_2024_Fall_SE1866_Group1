package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
