package project.dental_clinic_management.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import project.dental_clinic_management.entity.Branch;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WaitingRoomRequest {
    int waitingRoomID;
    boolean available;
    int numberPatient;
    Branch branch;
    int capacity;
}
