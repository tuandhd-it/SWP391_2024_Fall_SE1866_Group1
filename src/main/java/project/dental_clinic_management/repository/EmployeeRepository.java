package project.dental_clinic_management.repository;

import project.dental_clinic_management.entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    public Employee findByEmail(String email);
    public Employee findByPhone(String phone);
    @Query("SELECT e FROM Employee e WHERE e.emp_id = ?1")
    public Employee findByEmp_id(int emp_id);

    @Transactional
    @Modifying
    @Query("update Employee e set e.password = ?2 where e.email = ?1")
    void updatePassword(String email, String newPassword);

    @Query("SELECT e from Employee e WHERE (e.is_active = false) AND (e.first_name LIKE %?1% OR e.last_name LIKE %?1% OR LOWER(e.email) = LOWER(?1) OR e.phone LIKE %?1% OR e.address LIKE %?1%)")
    List<Employee> searchInactiveEmployee(String keyword);

    @Query("SELECT e FROM Employee e WHERE e.first_name LIKE %?1% OR e.last_name LIKE %?1% OR e.phone LIKE %?1% OR LOWER(e.email) = LOWER(?1)")
    List<Employee> findByNameContainingOrPhoneContaining( String Keyword);


    @Query("SELECT e FROM Employee e WHERE e.first_name LIKE %?1% OR e.last_name LIKE %?1% OR e.phone LIKE %?1%")
    List<Employee> findByNameContainingOrIdContaining(String Keyword);

    @Transactional
    @Modifying
    @Query("update Employee e set e.password = ?2 where e.emp_id = ?1")
    void updatePassword(int empId, String newPassword);



}
