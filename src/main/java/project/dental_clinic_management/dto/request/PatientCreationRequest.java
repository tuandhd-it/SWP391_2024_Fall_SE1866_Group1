package project.dental_clinic_management.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientCreationRequest {
    String firstName;
    String lastName;
    String gender;
    LocalDate dob;
    String address;
    String phone;
    String email;
    String medicalHistory;
}
