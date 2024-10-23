package project.dental_clinic_management.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ViewDoctorInfoRequest {
    int emp_id;
    String doctorName;
    String email;
    String phone;
    LocalDate dob;
    String gender;
    String roleName;
    String img;
}
