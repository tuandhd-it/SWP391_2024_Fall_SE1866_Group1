package project.dental_clinic_management.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import project.dental_clinic_management.entity.Medicine;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicineImportRequest {
        int regNumber;
        String medicineName;
        String quantity;
        String unit;
        double price;
        String ingredients;
        LocalDate date;

}

