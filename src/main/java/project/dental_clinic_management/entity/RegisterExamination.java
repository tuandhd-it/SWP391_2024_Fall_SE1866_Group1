package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class RegisterExamination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long regId;
    String firstName;
    String lastName;
    String email;
    String phone;
    String reason;
    Date dob;
    String gender;
    Date examRegisterDate;
    String note;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn (name = "bran_id")
    private Branch branch;

    public RegisterExamination(String firstName, String lastName, String email, String phone, String reason, Date dob, String gender, Date examRegisterDate, String note) {
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
