package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.dental_clinic_management.entity.Equipment;
import project.dental_clinic_management.entity.MedicineImport;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicineImportRepository extends JpaRepository<MedicineImport, Integer> {
    @Query("SELECT i FROM MedicineImport i WHERE i.medicineName LIKE %?1%")
    List<MedicineImport> findByMedImportNameContaining(@Param("name") String name);
}
