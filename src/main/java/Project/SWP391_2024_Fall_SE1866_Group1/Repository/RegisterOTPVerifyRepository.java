package Project.SWP391_2024_Fall_SE1866_Group1.Repository;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Employee;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.RegisterOTPVerify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterOTPVerifyRepository extends JpaRepository<RegisterOTPVerify, Integer> {

    @Query("select rv from RegisterOTPVerify rv where rv.otp = ?1 and rv.email = ?2")
    Optional<RegisterOTPVerify> findByOTPAndEmail(Integer otp, String email);

    RegisterOTPVerify findByOtp(Integer otp);
}
