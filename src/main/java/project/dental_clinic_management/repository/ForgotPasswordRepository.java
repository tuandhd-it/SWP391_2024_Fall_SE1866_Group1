package project.dental_clinic_management.repository;

import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.ForgotPassword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForgotPasswordRepository extends CrudRepository<ForgotPassword, Integer> {
    ForgotPassword findByEmployeeAndOtp(Employee employee, Integer otp);

    ForgotPassword findByOtp(Integer otp);
}
