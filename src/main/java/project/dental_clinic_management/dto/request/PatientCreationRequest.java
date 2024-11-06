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
    @Length (min = 2, max = 45,message = "Tên phải từ 2-45 ký tự")
    String firstName;
    @NotNull
    @NotBlank
    @Length (min = 2, max = 45,message = "Tên phải từ 2-45 ký tự")
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
    @Length (min = 10, max = 11, message = "SĐT phải đủ 10-11 số")
    String phone;
    @NotNull
    @NotBlank
    @Length (min = 10, max = 100, message = "Email phải từ 10 đến 100 ký tự")
    @Email(message = "Sai định dạng Email")
    String email;
    @Length (max = 225)
    String medicalHistory;
}
