package project.dental_clinic_management.entity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int equipmentId;
    String equipmentName;
    int quantity;
    String unit;
    double price;
    String note;

    public Equipment(String equipmentName, Integer quantity, String unit, double price, String note) {
        this.equipmentName = equipmentName;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.note = note;
    }
}
