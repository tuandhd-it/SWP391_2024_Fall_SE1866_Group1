package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.MedicineImport;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface MedicineImportRepository extends JpaRepository<MedicineImport, Integer> {

}

