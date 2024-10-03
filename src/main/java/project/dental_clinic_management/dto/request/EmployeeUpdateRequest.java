package project.dental_clinic_management.dto.request;

import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Role;
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
    double salary;
    String img;
    boolean active;
    String description;
    int branch_id;
    Role role;
}
