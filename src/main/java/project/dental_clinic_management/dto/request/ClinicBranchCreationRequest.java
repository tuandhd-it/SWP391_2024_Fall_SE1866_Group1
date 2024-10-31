package project.dental_clinic_management.dto.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClinicBranchCreationRequest {

    @NonNull
    @NotBlank
    @Length(min = 3, max = 30)
    String branchName;

    @NonNull
    @NotBlank
    @Length(min = 3, max = 30)
    String branch_description;

    @NonNull
    @NotBlank
    @Length(min = 3, max = 255)
    String branch_address;

    @NonNull
    @NotBlank
    @Length(min = 10, max = 12)
    String branch_phone;

    @NonNull
    @NotBlank
    String branch_img;
}
