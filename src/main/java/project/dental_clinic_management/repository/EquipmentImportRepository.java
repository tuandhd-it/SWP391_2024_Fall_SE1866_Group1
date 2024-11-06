package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import project.dental_clinic_management.entity.EquipmentImport;

public interface EquipmentImportRepository extends JpaRepository<EquipmentImport, Integer> {

}

