package project.dental_clinic_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.Record;

import java.util.List;


@Repository
public interface RecordRepository extends JpaRepository<Record, Integer> {
    Page<Record> getRecordByPatient_PatientId(int patientId, Pageable pageable);
    List<Record> getRecordByPatient_PatientId(int patientId);
    @Query("select r from Record r where r.patient.patientId = ?1 ")
    List<Record> findByPatient_PatientId(int patientId);
    Record findRecordByRecordId(long recordId);
}
