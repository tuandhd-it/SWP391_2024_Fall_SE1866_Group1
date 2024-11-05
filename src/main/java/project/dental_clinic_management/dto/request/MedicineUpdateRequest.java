package project.dental_clinic_management.dto.request;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedicineUpdateRequest {
    private String regNumber;
    private String name;
    private int quantity;
    private String unit;
    private double price;
    private String ingredients;

}
