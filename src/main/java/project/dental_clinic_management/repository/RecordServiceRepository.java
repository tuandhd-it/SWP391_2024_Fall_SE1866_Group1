package project.dental_clinic_management.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.Record;
import project.dental_clinic_management.entity.RecordService;

import java.util.List;


@Repository
public interface RecordServiceRepository extends JpaRepository<RecordService, Integer> {
    List<RecordService> findRecordServicesByRecord_RecordId(long id);
}
