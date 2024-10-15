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
public class EquipmentImportBill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int equipImportId;
    LocalDate equipImportDate;
    String note;
    double totalPrice;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "bran_id")
    private Branch branch;

    public EquipmentImportBill(LocalDate equipImportDate, String note, double totalPrice) {
        this.equipImportDate = equipImportDate;
        this.note = note;
        this.totalPrice = totalPrice;
    }
}
