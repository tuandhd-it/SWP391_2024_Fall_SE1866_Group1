package project.dental_clinic_management.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import project.dental_clinic_management.entity.Employee;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BranchEmployeeResponse {

    int employeeId;
    String employeeFullName;
}
