package project.dental_clinic_management.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.dental_clinic_management.dto.request.*;
import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.entity.Record;
import project.dental_clinic_management.repository.*;
import project.dental_clinic_management.service.AdminService;
import project.dental_clinic_management.service.CustomUserDetailService;
import project.dental_clinic_management.service.ReceptionistService;
import project.dental_clinic_management.service.RecordService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Copyright(C) 2005, Group 1 - SE1864
 *  Dental Clinic Management
 *
 *  Record of change
 *  Date        Version     Author              Description
 *  2024-09-20  1.0         Nguyen Viet Lam     Admin controller
 */
@Controller
@RequestMapping("/doctor")

/**
 * The class contain method to navigate to show information
 * Staff table in database. In the update or insert method, all data will be normalized (trim space) before update/insert into database
 * The methods will navigate to a error page if it have error
 *
 * @author: Nguyen Viet Lam
 */
public class DoctorController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ReceptionistService receptionistService;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private CustomUserDetailService customUserDetailService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private RecordMedicineRepository recordMedicineRepository;
    @Autowired
    private RecordServiceRepository recordServiceRepository;
    @Autowired
    private PatientWaitingRoomRepository patientWaitingRoomRepository;

    /**
     * Get data and bring it to page
     * @param userDetails
     * @param page
     * @param model
     * @return path to page
     */
    @GetMapping("/patientList")
    public String patientList(@AuthenticationPrincipal UserDetails userDetails,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "searchQuery", required = false) String searchQuery,
                              Model model) {

        String username = userDetails.getUsername();
        Employee receptionist = adminService.findByUsername(username);

        WaitingRoom waitingRoom = adminService.findWaitingRoomByBranchId(receptionist.getBranch().getBran_id());
        int waitingRoomId = waitingRoom.getWaitingRoomID();

        Page<PatientWaitingRoom> patientWaitingRequests;
        if (searchQuery != null && !searchQuery.isEmpty()) {
            patientWaitingRequests = receptionistService.searchPatientsInWaitingRoomByName(page, waitingRoomId, searchQuery);
            model.addAttribute("searchQuery", searchQuery);
        } else {
            patientWaitingRequests = receptionistService.getAllPatientWaitingRequestsInRoomIsWaiting(page, waitingRoomId);
        }

        model.addAttribute("listPatient", patientWaitingRequests.getContent());
        model.addAttribute("waitingRoomId", waitingRoomId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", patientWaitingRequests.getTotalPages());
        model.addAttribute("hasPrevious", patientWaitingRequests.hasPrevious());
        model.addAttribute("hasNext", patientWaitingRequests.hasNext());

        return "/employee/doctorTakePatient";
    }


    @GetMapping("/managePatient")
    public String getAllPatient(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        // Retrieve paginated list of patients
        Page<Patient> list = adminService.getPatientPaging(page, size);

        // Add attributes to the model for use in the view
        model.addAttribute("patients", list);
        model.addAttribute("totalPatient", list.getTotalElements());
        model.addAttribute("start", page * size + 1);
        model.addAttribute("end", Math.min((page + 1) * size, (int) list.getTotalElements()));

        return "/patient/managePatient";
    }


    @GetMapping("/newRecord/{patientId}")
    public String createNewRecord(@PathVariable("patientId") Integer patientId,
                                  @RequestParam(required = false) Integer patientWaitingId,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        String username = userDetails.getUsername();
        Employee employee = customUserDetailService.findByUsername(username);
        model.addAttribute("doctor", employee);
        Patient patient = adminService.getPatient(patientId);
        model.addAttribute("patient", patient);
        List<Medicine> medicines = medicineRepository.findAll();
        model.addAttribute("medicines", medicines);
        List<Service> servicesList = serviceRepository.findAll();
        List<PassDataServiceDTO>  services = new ArrayList<>();
        for (Service s:servicesList){
            services.add(new PassDataServiceDTO(s.getServiceId(),s.getServiceName(),s.getPrice()));
        }
        model.addAttribute("services", services);
        if(model.getAttribute("newRecord")==null){
            RecordCreationRequest newRecord = new RecordCreationRequest();
            for (int i=0;i<=10;i++){
                newRecord.addService(new ServiceRecordDTO());
                newRecord.addMedicine(new MedicineRecordDTO());
            }
            newRecord.setBranch_id(employee.getBranch().getBran_id());
            newRecord.setDoctorId(employee.getEmp_id());
            model.addAttribute("newRecord", newRecord);
        }
        model.addAttribute("patientWaitingId",patientWaitingId);
        return "patient/createRecord";
    }


    @PostMapping("/createRecord")
    public String createRecord(@ModelAttribute @Valid RecordCreationRequest newRecord,
                               BindingResult bindingResult,
                               @RequestParam(required = false) Integer patientWaitingId,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        int patientId = newRecord.getPatientId();
        if(bindingResult.hasErrors()){
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(
                    error -> errors.put(error.getField(), error.getDefaultMessage())
            );

            StringBuilder errorMsg = new StringBuilder();
            errorMsg.append("<b>Tạo hồ sơ thất bại</b>").append("<br><br>");

            for (String key : errors.keySet()) {
                errorMsg.append("<b>"+key+"</b>").append(": ").append(errors.get(key)).append("<br>");
            }
            redirectAttributes.addFlashAttribute("newRecord",newRecord);
            redirectAttributes.addFlashAttribute("errors", errorMsg.toString());
            return "redirect:/doctor/newRecord/"+patientId;
        }
        recordService.addRecord(newRecord);

        redirectAttributes.addFlashAttribute("errors", "Tạo hồ sơ bệnh nhân thành công!<b>");
        PatientWaitingRoom patient = patientWaitingRoomRepository.findById(patientWaitingId).get();
        patient.setStatus("Done");
        patientWaitingRoomRepository.save(patient);
        return "redirect:/doctor/listRecord/"+patientId;
    }

    @GetMapping("recordDetails/{id}")
    public String getRecordDetails(@PathVariable("id") Long recordId, Model model ){
        Record record = recordService.getRecordById(recordId);
        List<project.dental_clinic_management.entity.RecordService> recordServices = recordServiceRepository.findRecordServicesByRecord_RecordId(recordId);
        List<RecordMedicine> recordMedicines = recordMedicineRepository.findRecordMedicinesByRecordId_RecordId(recordId);
        model.addAttribute("record",record);
        model.addAttribute("services",recordServices);
        model.addAttribute("medicines",recordMedicines);
        return "patient/recordDetails";
    }

    @GetMapping("/listRecord/{id}")
    public String getRecordOfPatient(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Boolean isGetPatient,
            @RequestParam(required = false) Integer patientWaitId,
            Model model) {

        Page<Record> records = recordService.getAllRecordsByPatientID(id, page, size);
        model.addAttribute("records", records);
        model.addAttribute("totalRecord", records.getTotalElements());
        model.addAttribute("patientId", id);
        model.addAttribute("start", page * size + 1);
        model.addAttribute("end", Math.min((page + 1) * size, (int) records.getTotalElements()));

        if (Boolean.TRUE.equals(isGetPatient)) {
            model.addAttribute("isGetPatient", true);
            PatientWaitingRoom patient = patientWaitingRoomRepository.findById(patientWaitId).get();
            patient.setStatus("Processing");
            model.addAttribute("patientWaitingRoom", patient);
            patientWaitingRoomRepository.save(patient);
        }
        return "/patient/manageRecord";
    }

}
