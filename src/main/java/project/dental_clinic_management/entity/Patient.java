package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
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

    public Patient(String firstName, String lastName, String gender, LocalDate dob, String address, String phone, String email, String medicalHistory) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.dob = dob;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.medicalHistory = medicalHistory;
    }
}
