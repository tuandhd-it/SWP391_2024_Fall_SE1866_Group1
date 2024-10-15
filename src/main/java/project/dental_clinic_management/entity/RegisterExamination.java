package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RegisterExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long regId;
    String firstName;
    String lastName;
    String email;
    String phone;
    String reason;
    LocalDate dob;
    String gender;
    LocalDate examRegisterDate;
    String note;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn (name = "bran_id")
    private Branch branch;

    public RegisterExamination(String firstName, String lastName, String email, String phone, String reason, LocalDate dob, String gender, LocalDate examRegisterDate, String note) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.reason = reason;
        this.dob = dob;
        this.gender = gender;
        this.examRegisterDate = examRegisterDate;
        this.note = note;
    }
}
