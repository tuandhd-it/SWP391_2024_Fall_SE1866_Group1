package project.dental_clinic_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import project.dental_clinic_management.dto.request.MedicineRecordDTO;
import project.dental_clinic_management.dto.request.RecordCreationRequest;
import project.dental_clinic_management.dto.request.ServiceRecordDTO;
import project.dental_clinic_management.entity.Medicine;
import project.dental_clinic_management.entity.RecordMedicine;
import project.dental_clinic_management.repository.*;
import project.dental_clinic_management.entity.Record;

import java.util.List;

@Service
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RecordMedicineRepository recordMedicineRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private RecordServiceRepository recordServiceRepository;

    public Page<Record> getAllRecordsByPatientID(int patientID, int page, int size) {
        return recordRepository.getRecordByPatient_PatientId(patientID, PageRequest.of(page, size));
    }

    public List<Record> getAllRecordsByPatientID(int patientID) {
        return recordRepository.getRecordByPatient_PatientId(patientID);
    }

    public void addRecord(RecordCreationRequest recordRequest) {
        Record record = Record.builder()
                .recordDate(recordRequest.getDate())
                .branch(branchRepository.findById(recordRequest.getBranch_id()).get())
                .diagnose(recordRequest.getDiagnose())
                .note(recordRequest.getNote())
                .reason(recordRequest.getReason())
                .employee(adminService.getEmployeeById(recordRequest.getDoctorId()))
                .patient(adminService.getPatient(recordRequest.getPatientId()))
                .build();
        record = recordRepository.save(record);
        for(MedicineRecordDTO medicine: recordRequest.getMedicines()){
            Medicine med = medicineRepository.getReferenceById(medicine.getId());
            recordMedicineRepository.save(RecordMedicine.builder()
                            .recordId(record)
                            .note(medicine.getNote())
                            .quantity(medicine.getQuantity())
                            .price(med.getPrice())
                            .regNumber(med)
                    .build());
        }
        for(ServiceRecordDTO serviceRecordDTO: recordRequest.getServices()){
            project.dental_clinic_management.entity.Service service = serviceService.getServiceById(serviceRecordDTO.getId());
            recordServiceRepository.save(project.dental_clinic_management.entity.RecordService.builder()
                            .service(service)
                            .record(record)
                            .note(serviceRecordDTO.getNote())
                    .build());
        }

    }
}
