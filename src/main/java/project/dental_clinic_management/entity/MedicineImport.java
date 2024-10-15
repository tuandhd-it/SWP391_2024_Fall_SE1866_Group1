package project.dental_clinic_management.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor

public class MedicineImport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int medImportId;

    @ManyToOne
    @JoinColumn(name = "regNumber", nullable = false)
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "empId")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "branchId")
    private Branch branch;

    @Temporal(TemporalType.TIMESTAMP)
    Date date;

    String quantity;
    double price;
    String note;
    double totalPrice;

    public MedicineImport(Medicine medicine, Employee employee, Branch branch, Date date, String quantity, double price, String note, double totalPrice) {
        this.medicine = medicine;
        this.employee = employee;
        this.branch = branch;
        this.date = date;
        this.quantity = quantity;
        this.price = price;
        this.note = note;
        this.totalPrice = totalPrice;
    }
}
