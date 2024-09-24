package Project.SWP391_2024_Fall_SE1866_Group1.dto.request;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Role;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

//Use this instead of entity
public class ReceptionistCreationRequest {

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
    String branch_name;
    Role role;
}
