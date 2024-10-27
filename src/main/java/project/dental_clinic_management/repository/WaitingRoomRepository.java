package project.dental_clinic_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.dental_clinic_management.entity.WaitingRoom;

import java.util.List;

public interface WaitingRoomRepository extends JpaRepository<WaitingRoom, Integer> {

    WaitingRoom findWaitingRoomByWaitingRoomID(int id);

    @Query("SELECT wr FROM WaitingRoom wr WHERE LOWER(wr.branch.branchName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<WaitingRoom> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
