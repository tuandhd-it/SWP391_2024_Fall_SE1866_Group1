package project.dental_clinic_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import project.dental_clinic_management.entity.Medicine;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RecordCreationRequest {

    @NotNull
    int patientId;

    @NotNull
    int doctorId;

    @NotNull
    LocalDate date;

    @Length(max = 255)
    String reason;

    @Length(max = 255)
    String diagnose;

    @Length(max = 255)
    String note;

    @NotNull
    int branch_id;

    List<MedicineRecordDTO> medicines = new ArrayList<>();
    List<ServiceRecordDTO> services = new ArrayList<>();

    public void addMedicine(MedicineRecordDTO medicine){
        this.medicines.add(medicine);
    }

    public void addService(ServiceRecordDTO service){
        this.services.add(service);
    }
}
