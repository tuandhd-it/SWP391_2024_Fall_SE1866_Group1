package project.dental_clinic_management.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.Medicine;
import project.dental_clinic_management.entity.Role;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Employee findByEmail(String email);
    Employee findByPhone(String phone);
    @Query("SELECT e FROM Employee e WHERE e.emp_id = ?1")
    Employee findByEmp_id(int emp_id);

    @Transactional
    @Modifying
    @Query("update Employee e set e.password = ?2 where e.email = ?1")
    void updatePassword(String email, String newPassword);

    @Query("SELECT e from Employee e WHERE (e.is_active = false) AND e.email LIKE %?1%")
    List<Employee> searchInactiveEmployee(String keyword);

    @Query("SELECT e FROM Employee e WHERE e.first_name LIKE %?1% OR e.last_name LIKE %?1% OR e.phone LIKE %?1% OR LOWER(e.email) = LOWER(?1)")
    List<Employee> findByNameContainingOrPhoneContaining( String Keyword);


    @Query("SELECT e FROM Employee e WHERE e.first_name LIKE %?1% OR e.last_name LIKE %?1% OR e.phone LIKE %?1%")
    List<Employee> findByNameContainingOrIdContaining(String Keyword);

    @Transactional
    @Modifying
    @Query("update Employee e set e.password = ?2 where e.emp_id = ?1")
    void updatePassword(int empId, String newPassword);

    @Query("SELECT e from Employee e WHERE e.is_active = false")
    Page<Employee> findAllInactive(Pageable pageable);

    @Query("SELECT e from Employee e WHERE (e.is_active = false) AND e.email LIKE %?1%")
    Page<Employee> searchPageInactiveEmployee(String keyword, Pageable pageable);


    List<Employee> findByRole(Role byId);

    @Query("SELECT e FROM Employee e WHERE e.emp_id = ?1")
    Employee findById(int empId);
}
