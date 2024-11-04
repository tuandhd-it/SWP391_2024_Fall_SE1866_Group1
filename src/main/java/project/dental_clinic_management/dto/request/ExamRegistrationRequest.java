package project.dental_clinic_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamRegistrationRequest {

    @NotNull
    @NotBlank
    @Length(min = 1, max = 45, message = "Độ dài họ phải từ 1 đến 45 ký tự.")
    String firstName;

    @NotNull
    @NotBlank
    @Length (min = 1, max = 45, message = "Độ dài tên phải từ 1 đến 45 ký tự.")
    String lastName;

    @Email(message = "Không đúng định dạng email.")
    String email;

    @Pattern(regexp = "^(0[1-9][0-9]{8})$", message = "Số điện thoại không hợp lệ. Vui lòng nhập số điện thoại gồm 10 chữ số, bắt đầu bằng 0.")
    String phone;

    @NotNull
    @NotBlank
    @Length (min = 9, message = "Nguyên nhân khám từ 9 ký tự trở lên.")
    String reason;
    LocalDate dob;
    String gender;
    LocalDate examRegisterDate;
    String note;
    String employeeId;
    String branchName;
    String shift;
    boolean accept;
}
