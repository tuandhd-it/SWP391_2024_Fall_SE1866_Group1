package project.dental_clinic_management.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.Medicine;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
    @Query("SELECT m FROM Medicine m WHERE LOWER(m.medicineName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Medicine> findByMedicineNameContaining(@Param("name") String name);

    @Query("SELECT m FROM Medicine m WHERE LOWER(m.medicineName) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Medicine> findAllMedicine(Pageable pageable);

}