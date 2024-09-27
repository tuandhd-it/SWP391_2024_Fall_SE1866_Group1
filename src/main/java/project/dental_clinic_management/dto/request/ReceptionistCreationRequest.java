package project.dental_clinic_management.dto.request;

import project.dental_clinic_management.entity.Role;
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
    String re_password;
    LocalDate dob;
    String gender;
    boolean active;
    boolean accept;
    String address;
    String status;
    double salary;
    String branch_name;
    Role role;
    String img;
    String description;
}
