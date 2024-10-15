package project.dental_clinic_management.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.Medicine;


@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Integer> {
}
