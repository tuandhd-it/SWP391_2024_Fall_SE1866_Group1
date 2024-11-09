package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import project.dental_clinic_management.entity.EquipmentImport;
import project.dental_clinic_management.entity.MedicineImport;

import java.util.List;

public interface EquipmentImportRepository extends JpaRepository<EquipmentImport, Integer> {
    @Query("SELECT a FROM EquipmentImport a WHERE a.equipmentName LIKE %?1%")
    List<EquipmentImport> findByEquImportNameContaining(@Param("name") String name);
}

