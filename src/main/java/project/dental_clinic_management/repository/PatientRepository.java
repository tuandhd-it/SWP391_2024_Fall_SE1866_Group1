package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.dental_clinic_management.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

    @Query("SELECT p from Patient p WHERE p.phone = ?1")
    Patient findByPhone(String phone);

}
