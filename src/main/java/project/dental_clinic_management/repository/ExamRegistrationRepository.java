package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.RegisterExamination;

@Repository
public interface ExamRegistrationRepository extends JpaRepository<RegisterExamination, Long> {
    public RegisterExamination findByRegId(Long regId);

}
