package project.dental_clinic_management.dto.request;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EquipmentUpdateRequest {
    int equipImportId;
    String equipmentName;
    String quantity;
    String unit;
    double price;
    int emp_id;
    int bran_id;
}
