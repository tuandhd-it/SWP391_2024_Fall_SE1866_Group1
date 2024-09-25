package Project.SWP391_2024_Fall_SE1866_Group1.dto.request;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Role;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EmployeeChangePasswordRequest {
    int id;

    @NotEmpty(message = "New password is required")
    @Size(min = 8, message = "New password must be at least 8 characters")
    String newPassword;

    @NotEmpty(message = "Confirm password is required")
    String confirmPassword;
}
