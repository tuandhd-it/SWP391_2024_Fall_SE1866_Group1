package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long recordId;
    LocalDate recordDate;
    String reason;
    String diagnose;
    String note;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "bran_id")
    private Branch branch;

    @ManyToOne
    @JoinColumn(name = "patientId")
    private Patient patient;

    @OneToOne (mappedBy = "record")
    private Invoice invoice;

}
