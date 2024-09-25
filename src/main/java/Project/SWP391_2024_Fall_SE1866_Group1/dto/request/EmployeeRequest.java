package Project.SWP391_2024_Fall_SE1866_Group1.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeRequest {
    String first_name;
    String last_name;
    String email;
    String phone;
    String password;
    LocalDate dob;
    String gender;
    boolean is_active;
    boolean is_accept;
    String address;
    String status;
    double salary;
}
