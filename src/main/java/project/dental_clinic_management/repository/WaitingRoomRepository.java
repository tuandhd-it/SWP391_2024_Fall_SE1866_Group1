package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.dental_clinic_management.entity.WaitingRoom;

import java.util.List;

public interface WaitingRoomRepository extends JpaRepository<WaitingRoom, Integer> {

    WaitingRoom findWaitingRoomByWaitingRoomID(int id);
}
