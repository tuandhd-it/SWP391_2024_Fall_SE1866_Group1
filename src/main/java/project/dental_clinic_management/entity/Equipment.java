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
    String quantity;
    String unit;

    public Equipment(String equipmentName, String quantity, String unit) {

        this.equipmentName = equipmentName;
        this.quantity = quantity;
        this.unit = unit;
    }
}
