package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.RegisterExamination;

import java.util.List;

@Repository
public interface ExamRegistrationRepository extends JpaRepository<RegisterExamination, Long> {
    RegisterExamination findByRegId(Long regId);

    @Query("SELECT re from RegisterExamination re WHERE re.firstName LIKE %?1% OR re.lastName LIKE %?1% OR re.phone LIKE %?1% OR re.branch.branchName LIKE %?1%")
    List<RegisterExamination> searchRegisterExamination(String keyword);

}
