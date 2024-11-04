package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.dental_clinic_management.entity.PatientWaitingRoom;
import project.dental_clinic_management.entity.WaitingRoom;

import java.util.List;

public interface PatientWaitingRoomRepository extends JpaRepository<PatientWaitingRoom, Integer> {

    @Query("Select p From PatientWaitingRoom p where p.waitingRoomId.waitingRoomID = ?1")
    List<PatientWaitingRoom> findByWaitingRoomId(int waitingRoomId);

    @Query("SELECT COUNT(p) FROM PatientWaitingRoom p WHERE p.waitingRoomId = :waitingRoom")
    int countByWaitingRoom(@Param("waitingRoom") WaitingRoom waitingRoom);

    @Query("SELECT pwr FROM PatientWaitingRoom pwr WHERE pwr.waitingRoomId = :waitingRoomId AND " +
            "(LOWER(CONCAT(pwr.patient.firstName, ' ', pwr.patient.lastName)) LIKE LOWER(CONCAT('%', :patientName, '%')))")
    List<PatientWaitingRoom> findByWaitingRoomIdAndPatientNameContainingIgnoreCase(int waitingRoomId, String patientName);
}
