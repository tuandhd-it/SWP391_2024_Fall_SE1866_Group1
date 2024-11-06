package project.dental_clinic_management.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.Record;
import project.dental_clinic_management.entity.RecordMedicine;

import java.util.List;


@Repository
public interface RecordMedicineRepository extends JpaRepository<RecordMedicine, Integer> {
    List<RecordMedicine> findRecordMedicinesByRecordId_RecordId(long id);
}
