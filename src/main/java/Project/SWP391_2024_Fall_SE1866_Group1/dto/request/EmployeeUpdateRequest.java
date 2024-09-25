package Project.SWP391_2024_Fall_SE1866_Group1.dto.request;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeUpdateRequest {
    int emp_id;
    String first_name;
    String last_name;
    String email;
    String phone;
    LocalDate dob;
    String gender;
    String address;
    String status;
    double salary;
    Role role;
}
