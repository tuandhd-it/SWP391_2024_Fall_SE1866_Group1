package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.Query;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.ForgotPassword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForgotPasswordRepository extends CrudRepository<ForgotPassword, Integer> {
    List<ForgotPassword> findByEmployeeAndOtp(Employee employee, Integer otp);

    ForgotPassword findByOtp(Integer otp);

    @Query("SELECT fp FROM ForgotPassword fp WHERE fp.employee.email = ?1")
    ForgotPassword findByEmail(String email);
}
