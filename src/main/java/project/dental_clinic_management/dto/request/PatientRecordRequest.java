package project.dental_clinic_management.dto.request;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.Invoice;
import project.dental_clinic_management.entity.Patient;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientRecordRequest {
    long recordId;
    LocalDate recordDate;
    String reason;
    String diagnose;
    String note;
    int emp_id;
    int bran_id;
    int patientId;

}
