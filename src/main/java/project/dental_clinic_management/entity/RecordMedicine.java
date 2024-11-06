package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RecordMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int reMedicineId;
    int quantity;
    double price;
    String note;

    @ManyToOne
    @JoinColumn(name = "medicine_id", insertable = false, updatable = false)
    Medicine regNumber;

    @ManyToOne
    @JoinColumn (name = "recordID")
    Record record;

}
