package project.dental_clinic_management.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import project.dental_clinic_management.entity.Patient;
import project.dental_clinic_management.entity.WaitingRoom;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PatientWaitingRoomRequest {
    int id;
    LocalDate waitingDate;
    String status;
    String note;
    boolean isBooked;
    boolean isUrgency;
    WaitingRoom waitingRoom;
    Patient patient;
}
