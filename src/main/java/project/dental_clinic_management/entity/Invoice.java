package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int invoiceId;
    double totalBill;
    Date invoiceDate;
    String paymentMethod;

    @OneToOne
    private Record record;

    @ManyToOne
    @JoinColumn (name = "emp_id")
    private Employee employee;

    public Invoice(double totalBill, Date invoiceDate, String paymentMethod) {
        this.totalBill = totalBill;
        this.invoiceDate = invoiceDate;
        this.paymentMethod = paymentMethod;
    }
}
