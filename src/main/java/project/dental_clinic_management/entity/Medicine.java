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
public class Medicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int regNumber;
    String medicineName;
    String quantity;
    String unit;
    double price;
    String ingredients;



    // Constructors

    public Medicine(String medicineName, String quantity, String unit, double price, String ingredients) {

        this.medicineName = medicineName;
        this.quantity = quantity;
        this.unit = unit;
        this.price = price;
        this.ingredients = ingredients;
    }


}