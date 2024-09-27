package Project.SWP391_2024_Fall_SE1866_Group1.Repository;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Employee;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    public Employee findByEmail(String email);
    public Employee findByPhone(String phone);

    @Transactional
    @Modifying
    @Query("update Employee e set e.password = ?2 where e.email = ?1")
    void updatePassword(String email, String newPassword);
}
