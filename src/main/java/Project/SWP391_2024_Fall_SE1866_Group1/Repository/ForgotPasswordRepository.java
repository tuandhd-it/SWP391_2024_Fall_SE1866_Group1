package Project.SWP391_2024_Fall_SE1866_Group1.Repository;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Employee;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.ForgotPassword;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ForgotPasswordRepository extends CrudRepository<ForgotPassword, Integer> {
    ForgotPassword findByEmployeeAndOtp(Employee employee, Integer otp);

    ForgotPassword findByOtp(Integer otp);
}
