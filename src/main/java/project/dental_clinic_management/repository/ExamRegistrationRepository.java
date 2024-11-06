package project.dental_clinic_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.RegisterExamination;

import java.util.List;

@Repository
public interface ExamRegistrationRepository extends JpaRepository<RegisterExamination, Long> {
    RegisterExamination findByRegId(Long regId);

    @Query("SELECT re from RegisterExamination re WHERE re.accept = true AND re.inWaitingRoom = false AND re.phone LIKE %?1%")
    List<RegisterExamination> searchRegisterExaminationNotInWaitingRoom(String keyword);

    @Query("SELECT re from RegisterExamination re WHERE re.accept = false AND re.phone LIKE %?1%")
    List<RegisterExamination> searchPagePendingRegisterExamination(String keyword);
}
