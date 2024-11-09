package project.dental_clinic_management.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.Equipment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    @Query("SELECT e FROM Equipment e WHERE LOWER(e.equipmentName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Equipment> findByEquipmentNameContaining(@Param("name") String name);
}