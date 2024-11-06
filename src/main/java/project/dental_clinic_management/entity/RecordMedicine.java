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
@Builder
public class RecordMedicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int record_medicine_id;
    int quantity;
    double price;
    String note;

    @ManyToOne
    @JoinColumn(name = "medicine_id")
    Medicine regNumber;

    @ManyToOne
    @JoinColumn (name = "recordID")
    Record recordId;

}
