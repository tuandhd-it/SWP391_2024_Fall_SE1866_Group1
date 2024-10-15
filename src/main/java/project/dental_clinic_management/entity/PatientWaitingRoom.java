package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class PatientWaitingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int waitingRoomID;
    LocalDate waitingDate;
    String status;
    String note;
    boolean isBooked;
    boolean isUrgency;

    @ManyToOne
    @JoinColumn (name = "patient_id")
    private Patient patient;

    public PatientWaitingRoom(LocalDate waitingDate, String status, String note, boolean isBooked, boolean isUrgency) {
        this.waitingDate = waitingDate;
        this.status = status;
        this.note = note;
        this.isBooked = isBooked;
        this.isUrgency = isUrgency;
    }
}
