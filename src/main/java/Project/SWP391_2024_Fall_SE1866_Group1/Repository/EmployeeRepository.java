package Project.SWP391_2024_Fall_SE1866_Group1.Repository;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
