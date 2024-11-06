package project.dental_clinic_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecordCreationRequest {

    @NonNull
    int patientId;

    @NonNull
    int doctorId;

    LocalDate date;

    @Length(max = 255)
    String reason;

    @Length(max = 255)
    String diagnose;

    String note;

    int branch_id;

    List<Integer> medicineIds;
    List<Integer> medicineQuantity;
    List<String> medicineNotes;

    List<Integer> serviceIds;
    List<String> serviceNotes;

}
