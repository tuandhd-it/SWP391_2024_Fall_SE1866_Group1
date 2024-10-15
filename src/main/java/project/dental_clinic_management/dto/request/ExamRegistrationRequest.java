package project.dental_clinic_management.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamRegistrationRequest {
    String firstName;
    String lastName;
    String email;
    String phone;
    String reason;
    LocalDate dob;
    String gender;
    LocalDate examRegisterDate;
    String note;
    String employeeId;
    String branchName;
}
