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
public class EquipmentImport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int equipImportId;

    @OneToOne
    @JoinColumn(name = "equipment_id")
    Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    Employee employee;

    @ManyToOne
    @JoinColumn(name = "bran_id")
    Branch branch;
    String equipmentName;
    LocalDate date;
    double totalPrice;
}
