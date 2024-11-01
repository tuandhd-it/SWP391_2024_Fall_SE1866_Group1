package project.dental_clinic_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.dental_clinic_management.repository.RecordRepository;
import project.dental_clinic_management.entity.Record;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    public Page<Record> getAllRecordsByPatientID(int patientID, int page, int size) {
        return recordRepository.getRecordByPatient_PatientId(patientID, PageRequest.of(page, size));
    }

}
