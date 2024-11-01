package project.dental_clinic_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientCreationRequest {
    @NotNull
    @NotBlank
    @Length (min = 1, max = 45)
    String firstName;
    @NotNull
    @NotBlank
    @Length (min = 1, max = 45)
    String lastName;
    @NotNull
    String gender;
    @NotNull
    LocalDate dob;
    @NotNull
    @NotBlank
    @Length (min = 1, max = 255)
    String address;
    @NotNull
    @NotBlank
    @Length (min = 10, max = 15)
    String phone;
    @NotNull
    @NotBlank
    @Length (min = 10, max = 225)
    @Email(message = "Sai dinh dang email")
    String email;
    @Length (max = 225)
    String medicalHistory;
}
