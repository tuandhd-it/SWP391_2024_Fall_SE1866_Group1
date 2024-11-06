package project.dental_clinic_management.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.dental_clinic_management.dto.request.ExamRegistrationRequest;
import project.dental_clinic_management.dto.request.PatientWaitingRoomRequest;
import project.dental_clinic_management.dto.request.ViewExamRegistrationRequest;
import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.entity.Record;
import project.dental_clinic_management.entity.RecordService;
import project.dental_clinic_management.repository.*;
import project.dental_clinic_management.dto.request.ReceptionistCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReceptionistService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ExamRegistrationRepository examRegistrationRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientWaitingRoomRepository patientWaitingRoomRepository;
    @Autowired
    private RecordRepository recordRepository;
    @Autowired
    private RecordServiceRepository recordServiceRepository;
    @Autowired
    private MedicineRepository medicineRepository;
    @Autowired
    private RecordMedicineRepository recordMedicineRepository;
    @Autowired
    private InvoiceRepository invoiceRepository;


    public Employee findByUsername(String username) {
        return employeeRepository.findByEmail(username);
    }

    public Patient findPatientById(int id){
        return findPatientById(id);
    }

    //Create a new receptionist
    public void createReceptionist(ReceptionistCreationRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Employee employee = new Employee();

        //Create branch to store in receptionist information
        Branch branch = branchRepository.findByBranchName(request.getBranch_name());

        //Create account to store in receptionist information
        String password = encoder.encode(request.getPassword());
        employee.setPassword(password);
        employee.setDob(request.getDob());
        employee.setGender(request.getGender());
        employee.set_active(request.isActive());
        employee.setFirst_name(request.getFirstName());
        employee.setLast_name(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setAddress(request.getAddress());
        employee.setSalary(request.getSalary());
        employee.setImg(request.getImg());
        employee.setDescription(request.getDescription());
        employee.setBranch(branch);
        employee.setRole(request.getRole());
        employeeRepository.save(employee);
    }

    //Find Role by role_id
    public Role findRoleById(int id) {
        return roleRepository.findById(id);
    }

    //Find Role by roleName
    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    //Find all branch
    public List<Branch> findAllBranches() {
        return branchRepository.findAll();
    }

    //Find Employee has specific email
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    //Find Employee has specific phone number
    public Employee findByPhone(String phone) {
        return employeeRepository.findByPhone(phone);
    }

    //Check whether entered email, phone is existed
    public String checkExistedEmployee(String email, String phone) {
        Employee existedEmail = findByEmail(email);
        Employee existedPhone = findByPhone(phone);
        if (existedEmail != null) {
            return "Email already exists";
        } else if (existedPhone != null) {
            return "Phone already exists";
        }
        return null;
    }

    //Tìm tất cả bác sĩ thuộc chi nhánh của receptionist
    public List<Employee> findAllBranchDoctor(Employee receptionist) {
        List<Employee> doctors = new ArrayList<>();
        List<Employee> allEmployees = employeeRepository.findAll();
        for (Employee employee : allEmployees) {
            if (employee.getRole().getRoleName().equals("Doctor") && employee.getBranch().equals(receptionist.getBranch()) && employee.is_active()) {
                doctors.add(employee);
            }
        }
        return doctors;
    }

    //Tìm tất cả danh sách đăng ký khám thuộc chi nhánh của receptionist
    public List<ViewExamRegistrationRequest> findAllBranchExam(Employee receptionist) {
        List<ViewExamRegistrationRequest> exams = new ArrayList<>();
        List<RegisterExamination> registerExaminations = examRegistrationRepository.findAll();
        for (RegisterExamination registerExamination : registerExaminations) {
            if(registerExamination.getBranch().equals(receptionist.getBranch()) && !registerExamination.isAccept()) {
                exams.add(ViewExamRegistrationRequest.builder()
                                .firstName(registerExamination.getFirstName())
                                .lastName(registerExamination.getLastName())
                                .branchName(registerExamination.getBranch().getBranchName())
                                .phone(registerExamination.getPhone())
                                .examId(registerExamination.getRegId())
                        .build());
            }
        }
        return exams;
    }

    //Tìm tất cả danh sách đăng ký khám thuộc chi nhánh đã được phê duyệt của receptionist
    public List<ViewExamRegistrationRequest> findAllBranchExamAccept(Employee receptionist) {
        List<ViewExamRegistrationRequest> exams = new ArrayList<>();
        List<RegisterExamination> registerExaminations = examRegistrationRepository.findAll();
        for (RegisterExamination registerExamination : registerExaminations) {
            if(registerExamination.getBranch().equals(receptionist.getBranch()) && registerExamination.isAccept() && !registerExamination.isInWaitingRoom()) {
                exams.add(ViewExamRegistrationRequest.builder()
                        .firstName(registerExamination.getFirstName())
                        .lastName(registerExamination.getLastName())
                        .branchName(registerExamination.getBranch().getBranchName())
                        .phone(registerExamination.getPhone())
                        .examId(registerExamination.getRegId())
                        .build());
            }
        }
        return exams;
    }

    //Tìm thông tin khám bệnh qua regId
    public RegisterExamination findExamRegistrationByRegId(String regId) {
        return examRegistrationRepository.findByRegId(Long.parseLong(regId));
    }

    //Tìm bệnh nhân qua số điện thoại
    public Patient findPatientByPhone(String phone) {
        return patientRepository.findByPhone(phone);
    }

    //Xoá đơn khám của bệnh nhân
    public void deleteExam(RegisterExamination registerExamination) {
        examRegistrationRepository.delete(registerExamination);
    }

    //Lưu examinatioh
    public void createExamRegistration(ExamRegistrationRequest examRegistrationRequest) {
        Employee doctor = employeeRepository.findByEmp_id(Integer.parseInt(examRegistrationRequest.getEmployeeId()));
        Branch branch = branchRepository.findByBranchName(examRegistrationRequest.getBranchName());

        examRegistrationRepository.save(RegisterExamination.builder()
                        .firstName(examRegistrationRequest.getFirstName())
                        .lastName(examRegistrationRequest.getLastName())
                        .email(examRegistrationRequest.getEmail())
                        .phone(examRegistrationRequest.getPhone())
                        .reason(examRegistrationRequest.getReason())
                        .dob(examRegistrationRequest.getDob())
                        .gender(examRegistrationRequest.getGender())
                        .examRegisterDate(examRegistrationRequest.getExamRegisterDate())
                        .employee(doctor)
                        .branch(branch)
                        .note(examRegistrationRequest.getNote())
                        .accept(false)
                        .inWaitingRoom(false)
                .build());
    }

    //Search list examination registration
    public List<ViewExamRegistrationRequest> searchAllExamRegistration(String keyword) {
        List<RegisterExamination> list = examRegistrationRepository.searchRegisterExamination(keyword);
        List<ViewExamRegistrationRequest> viewRequestList = new ArrayList<>();

        for(RegisterExamination registerExamination : list) {
            viewRequestList.add(ViewExamRegistrationRequest.builder()
                    .examId(registerExamination.getRegId())
                    .firstName(registerExamination.getFirstName())
                    .lastName(registerExamination.getLastName())
                    .email(registerExamination.getEmail())
                    .phone(registerExamination.getPhone())
                    .reason(registerExamination.getReason())
                    .dob(registerExamination.getDob())
                    .gender(registerExamination.getGender())
                    .examRegisterDate(registerExamination.getExamRegisterDate())
                    .doctorName(registerExamination.getEmployee().getFirst_name() + " " + registerExamination.getEmployee().getLast_name())
                    .branchName(registerExamination.getBranch().getBranchName())
                    .note(registerExamination.getNote())
                    .build());
        }
        return viewRequestList;
    }

    //Tìm tất cả bác sĩ có ca trong ngày
    public List<Employee> findAllDoctorShift() {
        boolean shift;


        LocalDateTime currentTime = LocalDateTime.now();

        LocalDate currentDate = LocalDate.now();
        if(currentTime.isAfter(currentTime.with((LocalTime.of(7,15)))) && currentTime.isBefore(currentTime.with((LocalTime.of(11,30)))) ) {
            shift = false;
        } else if (currentTime.isAfter(currentTime.with((LocalTime.of(13,45)))) && currentTime.isBefore(currentTime.with((LocalTime.of(19,0)))) ) {
            shift = true;
        } else if (currentTime.isAfter(currentTime.with((LocalTime.of(11,30)))) && currentTime.isBefore(currentTime.with((LocalTime.of(13,45)))) ) {
            shift = true;
        } else {
            shift = false;
            currentDate = currentDate.plusDays(1);
        }

        return scheduleRepository.findEmployeeByShift(shift, currentDate);
    }

    //Tìm tất cả bác sĩ có ca trong ngày được chọn
    public List<Employee> findDoctorShiftForGuest(LocalDate choosenDate, String branchName, String shiftString) {
        boolean shift;
        shift = !shiftString.equalsIgnoreCase("morning");
        List<Employee> shiftedEmployeeList = scheduleRepository.findEmployeeByShift(shift, choosenDate);
        List<Employee> branchEmployeeShift = new ArrayList<>();
        for(Employee employee : shiftedEmployeeList) {
            if(employee.getBranch().getBranchName().equals(branchName) && employee.getRole().getRoleName().equalsIgnoreCase("Doctor")) {
                branchEmployeeShift.add(employee);
            }
        }
        return branchEmployeeShift;
    }

    //Tìm tất cả lịch làm việc theo empId
    public List<Schedule> getSchedulesByEmployeeId(int employeeId) {
        return scheduleRepository.findByEmpId(employeeId);
    }

    //Update trang thái của đơn (vào phòng chờ ?)
    public void updateInWaitingRoom(RegisterExamination registerExamination) {
        registerExamination.setInWaitingRoom(true);
        examRegistrationRepository.save(registerExamination);
    }

    /**
     * Get all patient waiting in room
     * @param index
     * @param waitingRoomId
     * @return list patient
     */
    public Page<PatientWaitingRoom> getAllPatientWaitingRequestsInRoomIsWaiting(int index, int waitingRoomId) {
        Pageable pageable = PageRequest.of(index - 1, 3);

        // Fetch all waiting requests for the waiting room
        List<PatientWaitingRoom> waitingRequests = patientWaitingRoomRepository.findByWaitingRoomId(waitingRoomId);

        // Sort the waiting requests: First by urgency (true first), then by booking status (true first)
        List<PatientWaitingRoom> filteredAndSortedRequests = waitingRequests.stream()
                .filter(request -> "Waiting".equals(request.getStatus())) // Filter only "Waiting" patients
                .sorted(Comparator.comparing(PatientWaitingRoom::isUrgency)
                        .thenComparing(PatientWaitingRoom::isBooked).reversed())
                .collect(Collectors.toList());

        // Use PageImpl to convert list to page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredAndSortedRequests.size());

        if (start > end) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return new PageImpl<>(filteredAndSortedRequests.subList(start, end), pageable, filteredAndSortedRequests.size());
    }


    public Page<PatientWaitingRoom> searchPatientsInWaitingRoomByName(int index, int waitingRoomId, String patientName) {
        Pageable pageable = PageRequest.of(index - 1, 3);

        // Fetch all patients in the waiting room by waitingRoomId and containing patientName
        List<PatientWaitingRoom> waitingRequests = patientWaitingRoomRepository.findByWaitingRoomIdAndPatientNameContainingIgnoreCase(waitingRoomId, patientName);

        // Filter and sort the waiting requests: Only "Waiting" status, sorted by urgency and booking status
        List<PatientWaitingRoom> filteredAndSortedRequests = waitingRequests.stream()
                .filter(request -> "Waiting".equals(request.getStatus())) // Filter for patients with status "Waiting"
                .sorted(Comparator.comparing(PatientWaitingRoom::isUrgency)
                        .thenComparing(PatientWaitingRoom::isBooked).reversed())
                .collect(Collectors.toList());

        // Create a pageable subset of the results
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredAndSortedRequests.size());

        if (start > end) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return new PageImpl<>(filteredAndSortedRequests.subList(start, end), pageable, filteredAndSortedRequests.size());
    }

    /**
     * Get all patient waiting in room
     * @param index
     * @param waitingRoomId
     * @return list patient
     */
    public Page<PatientWaitingRoom> getAllPatientWaitingRequestsInRoomIsDone(int index, int waitingRoomId) {
        Pageable pageable = PageRequest.of(index - 1, 3);

        // Fetch all waiting requests for the waiting room
        List<PatientWaitingRoom> waitingRequests = patientWaitingRoomRepository.findByWaitingRoomId(waitingRoomId);

        // Sort the waiting requests: First by urgency (true first), then by booking status (true first)
        List<PatientWaitingRoom> filteredAndSortedRequests = waitingRequests.stream()
                .filter(request -> "Waiting".equals(request.getStatus())) // Filter only "Waiting" patients
                .sorted(Comparator.comparing(PatientWaitingRoom::isUrgency)
                        .thenComparing(PatientWaitingRoom::isBooked).reversed())
                .collect(Collectors.toList());

        // Use PageImpl to convert list to page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredAndSortedRequests.size());

        if (start > end) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return new PageImpl<>(filteredAndSortedRequests.subList(start, end), pageable, filteredAndSortedRequests.size());
    }


    public Page<PatientWaitingRoom> searchPatientsInWaitingRoomByNameStatusDone(int index, int waitingRoomId, String patientName) {
        Pageable pageable = PageRequest.of(index - 1, 3);

        // Fetch all patients in the waiting room by waitingRoomId and containing patientName
        List<PatientWaitingRoom> waitingRequests = patientWaitingRoomRepository.findByWaitingRoomIdAndPatientNameContainingIgnoreCase(waitingRoomId, patientName);

        // Filter and sort the waiting requests: Only "Waiting" status, sorted by urgency and booking status
        List<PatientWaitingRoom> filteredAndSortedRequests = waitingRequests.stream()
                .filter(request -> "Done".equals(request.getStatus())) // Filter for patients with status "Done"
                .sorted(Comparator.comparing(PatientWaitingRoom::isUrgency)
                        .thenComparing(PatientWaitingRoom::isBooked).reversed())
                .collect(Collectors.toList());

        // Create a pageable subset of the results
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredAndSortedRequests.size());

        if (start > end) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return new PageImpl<>(filteredAndSortedRequests.subList(start, end), pageable, filteredAndSortedRequests.size());
    }

    public Record findRecordByPatientId(int patientId) {
        return recordRepository.findByPatientId(patientId);
    }

    public List<RecordService> findRecordServicesByRecordId(long id) {
        return recordServiceRepository.findRecordServicesByRecordId(id);
    }

    public List<RecordMedicine> findRecordMedicinesByRecordId(long id) {
        return recordMedicineRepository.findRecordMedicinesByRecordId(id);
    }

    public double totalAmout(List<RecordService> recordServices, List<RecordMedicine> recordMedicines) {
        double totalAmout = 0.0;
        for (RecordService recordService : recordServices) {
            totalAmout += recordService.getService().getPrice();
        }

        for (RecordMedicine recordMedicine : recordMedicines) {
            totalAmout += recordMedicine.getRegNumber().getPrice() * recordMedicine.getQuantity();
        }

        return totalAmout;
    }

    public Record getRecordByID(long id) {
        return recordRepository.findByRecordId(id);
    }

    public Invoice createInvoice(String uuid, String date, String totalAmount, String recordId, String paymentMethod){
        UUID invoiceUuid = UUID.fromString(uuid);

        // Chuyển đổi chuỗi ngày thành LocalDate
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // hoặc định dạng ngày của bạn
        LocalDate invoiceDate = LocalDate.parse(date, formatter);

        // Chuyển đổi chuỗi tổng tiền thành double
        double invoiceTotalAmount = Double.parseDouble(totalAmount);

        // Chuyển đổi recordId thành một số kiểu dữ liệu khác nếu cần
        int invoiceRecordId = Integer.parseInt(recordId);

        Record record = getRecordByID(invoiceRecordId);

        // Tạo đối tượng `Invoice` mới với các giá trị đã chuyển đổi
        Invoice invoice = new Invoice();
        invoice.setInvoiceId(invoiceUuid);
        invoice.setInvoiceDate(invoiceDate);
        invoice.setTotalBill(invoiceTotalAmount);
        invoice.setRecord(record);
        invoice.setPaymentMethod(paymentMethod);

        return invoiceRepository.save(invoice);
    }

}
