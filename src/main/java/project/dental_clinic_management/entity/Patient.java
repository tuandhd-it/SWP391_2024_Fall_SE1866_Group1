package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int patientId;
    String firstName;
    String lastName;
    String gender;
    LocalDate dob;
    String address;
    String phone;
    String email;
    String medicalHistory;

    @OneToMany (mappedBy = "recordId")
    private List<Record> records;

    @OneToMany (mappedBy = "patient")
    private List<PatientWaitingRoom> patientWaitingRooms;

}
