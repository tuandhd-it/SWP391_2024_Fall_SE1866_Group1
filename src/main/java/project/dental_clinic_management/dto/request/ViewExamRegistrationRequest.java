package project.dental_clinic_management.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewExamRegistrationRequest {
    long examId;
    String firstName;
    String lastName;
    String branchName;
    String phone;
    String reason;
    LocalDate dob;
    String gender;
    LocalDate examRegisterDate;
    String note;
    String doctorName;
    String email;
}
