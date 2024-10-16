package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.dental_clinic_management.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

}
