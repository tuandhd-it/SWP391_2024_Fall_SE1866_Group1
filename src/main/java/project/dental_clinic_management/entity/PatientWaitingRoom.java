package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
@Setter
public class PatientWaitingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    LocalDate waitingDate;
    String status;
    String note;
    boolean isBooked;
    boolean isUrgency;

    @ManyToOne
    @JoinColumn (name = "waitingRoomId")
    WaitingRoom waitingRoomId;

    @ManyToOne
    @JoinColumn (name = "patientId")
    Patient patient;

    public PatientWaitingRoom(LocalDate waitingDate, String status, String note, boolean isBooked, boolean isUrgency) {
        this.waitingDate = waitingDate;
        this.status = status;
        this.note = note;
        this.isBooked = isBooked;
        this.isUrgency = isUrgency;
    }
}
