package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class MedicineImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int medImportId; // Thêm trường này làm khóa chính cho MedicineImport

    @ManyToOne
    @JoinColumn(name = "reg_number")
    Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    Employee employee;

    @ManyToOne
    @JoinColumn(name = "bran_id")
    Branch branch;

    LocalDate date;
}
