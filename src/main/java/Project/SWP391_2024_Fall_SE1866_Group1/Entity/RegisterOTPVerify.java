package Project.SWP391_2024_Fall_SE1866_Group1.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RegisterOTPVerify {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rvid;

    @Column(nullable = false)
    private Integer otp;

    @Column(nullable = false)
    private Date expirationTime;

    private String email;

}
