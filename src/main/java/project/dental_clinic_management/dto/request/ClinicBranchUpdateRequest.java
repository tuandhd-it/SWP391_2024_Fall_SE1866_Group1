package project.dental_clinic_management.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClinicBranchUpdateRequest {
    int id;
    String branchName;
    String branch_description;
    String branch_address;
    String branch_phone;
    String branch_img;
}
