package project.dental_clinic_management.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ScheduleEmployeeInfoResponse {
    int scheduleId;
    LocalDate date;
    boolean shift;
    int employeeId;
    String employeeName;
    String currentUserRole;
    String employeeRole;

}
