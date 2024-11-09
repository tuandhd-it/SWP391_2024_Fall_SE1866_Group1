package project.dental_clinic_management.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.dental_clinic_management.dto.MailBody;
import project.dental_clinic_management.dto.request.*;
import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ManagerService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private ExamRegistrationRepository examRegistrationRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private MedicineImportRepository medicineImportRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private EquipmentImportRepository equipmentImportRepository;

    //Create Schedule
    public void createSchedule(ScheduleCreationRequest request) {
        scheduleRepository.save(Schedule.builder()
                        .date(request.getDate())
                        .shift(request.isShift())
                        .employee(employeeRepository.findByEmp_id(request.getEmployeeId()))
                .build());
    }

    public Employee findByUsername(String username) {
        return employeeRepository.findByEmail(username);
    }

    //Find employee by Id
    public Employee getEmployeeById(int id) {
        return employeeRepository.findByEmp_id(id);
    }

    //Find all Schedule
    public List<Schedule> getAllSchedules() {
        return scheduleRepository.findAll();
    }

    //Find schedule
    public List<Schedule> getSchedulesByEmployeeId(int employeeId) {
        return scheduleRepository.findByEmpId(employeeId);
    }
    //Tìm tất cả nhân viên thuộc chi nhánh của manager
    public List<Employee> findAllBranchEmployee(Employee manager) {
        List<Employee> employeeList = new ArrayList<>();
        List<Employee> allEmployees = employeeRepository.findAll();
        for (Employee employee : allEmployees) {
            if (!employee.getRole().getRoleName().equalsIgnoreCase("Manager") && !employee.getRole().getRoleName().equalsIgnoreCase("Admin") && employee.getBranch().equals(manager.getBranchManaged()) && employee.is_active()) {
                employeeList.add(employee);
            }
        }
        return employeeList;
    }

    //Kiểm tra xem nhân viên đó đã làm việc trong ca đó chưa
    public boolean checkExistedSchedule(ScheduleCreationRequest request) {
        List<Schedule> schedules = getSchedulesByEmployeeId(request.getEmployeeId());
        for(Schedule schedule : schedules) {
            if(schedule.getDate().equals(request.getDate()) && schedule.isShift() == request.isShift()) {
                return true;
            }
        }
        return false;
    }

    //Get examination registration by examId
    public RegisterExamination getExaminationById(Long examId) {
        return examRegistrationRepository.findByRegId(examId);

    }

    //Phê duyệt đơn đăng ký khám
    @Async
    public void acceptExamination(List<Long> examIdList) {
        for (Long examId : examIdList) {
            RegisterExamination registerExamination = getExaminationById(examId);
            if (registerExamination == null) {
                throw new RuntimeException("Customer with id " + examId + " not found.");
            }
            //Gửi mail ở đây
            //Tạo 1 tin nhắn mail để gửi tới email user
            MailBody mailBody = MailBody.builder()
                    .to(registerExamination.getEmail())
                    .text("Dear " + registerExamination.getFirstName() + " " + registerExamination.getLastName() + ",\n" +
                            "\n" +
                            "We are pleased to inform you that your examination registration request has been approved. Your booking is now confirmed, and we look forward to providing you with our services.\n" +
                            "\n" +
                            "Please check your email or contact our team if you need additional information about your appointment details.\n" +
                            "\n" +
                            "Thank you for choosing our services.\n" +
                            "\n" +
                            "Best regards,\n" +
                            "DCMS Team")
                    .subject("Your Examination Registration is Approved")
                    .build();

            emailService.sendSimpleMessage(mailBody);
            registerExamination.setAccept(true);
            examRegistrationRepository.save(registerExamination);
        }
    }

    //Reject đơn đăng ký khám
    @Async
    public void rejectExamination(List<Long> examIdList) {
        for (Long examId : examIdList) {
            RegisterExamination registerExamination = getExaminationById(examId);
            if (registerExamination == null) {
                throw new RuntimeException("Customer with id " + examId + " not found.");
            }

            //Gửi mail ở đây
            //Tạo 1 tin nhắn mail để gửi tới email user
            MailBody mailBody = MailBody.builder()
                    .to(registerExamination.getEmail())
                    .text("Dear " + registerExamination.getFirstName() + " " + registerExamination.getLastName() + ",\n" +
                            "\n" +
                            "We regret to inform you that your examination registration request has been declined as our clinic is fully booked for the selected time slot.\n" +
                            "\n" +
                            "We apologize for any inconvenience this may cause. Please feel free to choose an alternative date or contact our team for assistance in rescheduling your appointment.\n" +
                            "\n" +
                            "Thank you for your understanding and interest in our services.\n" +
                            "\n" +
                            "Best regards,\n" +
                            "DCMS Team")
                    .subject("Your Examination Registration Request")
                    .build();

            emailService.sendSimpleMessage(mailBody);
            examRegistrationRepository.delete(registerExamination);
        }
    }

    //Xoá lịch làm việc
    public void deleteScheduleByEmpIdAndScheduleId(int empId, LocalDate scheduleDate, boolean shift) {
        scheduleRepository.deleteEmpSchedule(scheduleDate, empId, shift);
    }

    //List medicine
    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }
    public void importMedicine(MedicineImportRequest request) {
        List<Medicine> existingMedicines = medicineRepository.findByMedicineNameContaining(request.getMedicineName());
        Medicine medicine;

        if (!existingMedicines.isEmpty()) {
            // Retrieve the first medicine if it exists
            medicine = existingMedicines.get(0);
        } else {
            // Save the new medicine if it does not exist
            medicine = new Medicine(
                    request.getMedicineName(),
                    request.getQuantity(),
                    request.getUnit(),
                    request.getPrice(),
                    request.getIngredients()
            );
            medicine = medicineRepository.save(medicine);
        }

        // Retrieve current employee information from the logged-in account
        Employee currentEmployee = getCurrentEmployee();

        // Create a MedicineImport record with import information
        MedicineImport medicineImport = new MedicineImport();
        medicineImport.setMedicine(medicine);
        medicineImport.setMedicineName(medicine.getMedicineName()); // Set medicineName field
        medicineImport.setTotalPrice(medicine.getQuantity() * medicine.getPrice());
        medicineImport.setEmployee(currentEmployee);
        medicineImport.setBranch(currentEmployee.getBranch());
        medicineImport.setDate(LocalDate.now());

        medicineImportRepository.save(medicineImport);
    }
    public Employee getCurrentEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomEmployeeDetails customEmployeeDetails = (CustomEmployeeDetails) authentication.getPrincipal();
        return customEmployeeDetails.getEmployee();
    }


    public List<MedicineImport> getAllMedicineImports() {
        return medicineImportRepository.findAll();
    }

    //update medicine

    public void updateMedicine(Medicine updatedMedicine) {

        Medicine existingMedicine = medicineRepository.findById(updatedMedicine.getRegNumber())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc để cập nhật"));

        // Cập nhật các thông tin của thuốc
        existingMedicine.setMedicineName(updatedMedicine.getMedicineName());
        existingMedicine.setQuantity(updatedMedicine.getQuantity());
        existingMedicine.setUnit(updatedMedicine.getUnit());
        existingMedicine.setPrice(updatedMedicine.getPrice());
        existingMedicine.setIngredients(updatedMedicine.getIngredients());

        // Lưu lại thông tin thuốc sau khi cập nhật
        medicineRepository.save(existingMedicine);
    }


    public ManagerService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }
    //search medicine
    public List<Medicine> searchMedicineByName(String name) {
        return medicineRepository.findByMedicineNameContaining(name);
    }
    //search medicine history
    public List<MedicineImport> searchMedImportByName(String name) {
        return medicineImportRepository.findByMedImportNameContaining(name);
    }
    //List equipment
    public List<Equipment> getAllEquipments() {
        return equipmentRepository.findAll();
    }

    public void importEquipment(EquipmentImportRequest request) {
        List<Equipment> existingEquipments = equipmentRepository.findByEquipmentNameContaining(request.getEquipmentName());
        Equipment equipment;

        if (!existingEquipments.isEmpty()) {
            // Retrieve the first equipment if it exists
            equipment = existingEquipments.get(0);
        } else {
            // Save the new equipment if it does not exist
            equipment = new Equipment(
                    request.getEquipmentName(),
                    request.getQuantity(),
                    request.getUnit(),
                    request.getPrice(),
                    request.getNote()
            );
            equipment = equipmentRepository.save(equipment);
        }

        // Retrieve current employee information from the logged-in account
        Employee currentEmployee = getCurrentEmployee();

        // Create a equipmentImport record with import information
        EquipmentImport equipmentImport = new EquipmentImport();
        equipmentImport.setEquipment(equipment);
        equipmentImport.setEquipmentName(equipment.getEquipmentName());
        equipmentImport.setTotalPrice(equipment.getQuantity() * equipment.getPrice());
        equipmentImport.setEmployee(currentEmployee);
        equipmentImport.setBranch(currentEmployee.getBranch());
        equipmentImport.setDate(LocalDate.now());

        equipmentImportRepository.save(equipmentImport);
    }

    public List<EquipmentImport> getAllEquipmentImports() {
        return equipmentImportRepository.findAll();
    }

    public void updateEquipment(Equipment updatedEquipment) {

        Equipment existingEquipment = equipmentRepository.findById(updatedEquipment.getEquipmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thuốc để cập nhật"));

        // Cập nhật các thông tin của equipment
        existingEquipment.setEquipmentName(updatedEquipment.getEquipmentName());
        existingEquipment.setQuantity(updatedEquipment.getQuantity());
        existingEquipment.setUnit(updatedEquipment.getUnit());
        existingEquipment.setPrice(updatedEquipment.getPrice());
        existingEquipment.setNote(updatedEquipment.getNote());

        // Lưu lại thông tin equipment sau khi cập nhật
        equipmentRepository.save(existingEquipment);
    }

    //search equipment
    public List<Equipment> searchEquipmentByName(String name) {
        return equipmentRepository.findByEquipmentNameContaining(name);
    }
    //search equipment history
    public List<EquipmentImport> searchEquImportByName(String name) {
        return equipmentImportRepository.findByEquImportNameContaining(name);
    }


}
