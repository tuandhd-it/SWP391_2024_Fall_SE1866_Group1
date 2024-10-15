package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
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

    public Record(LocalDate recordDate, String reason, String diagnose, String note) {
        this.recordDate = recordDate;
        this.reason = reason;
        this.diagnose = diagnose;
        this.note = note;
    }
}
