package project.dental_clinic_management.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
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
    @Length (min = 7, max = 45, message = "Độ dài mật khẩu phải từ 7 đến 45 ký tự.")
    String password;
    String re_password;
    LocalDate dob;
    String gender;
    boolean active;
    String address;
    double salary;
    String branch_name;
    Role role;
    String img;
    String description;
}
