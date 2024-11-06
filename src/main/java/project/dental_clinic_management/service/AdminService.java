package project.dental_clinic_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.dental_clinic_management.dto.request.*;
import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.repository.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private WaitingRoomRepository waitingRoomRepository;

    @Autowired
    private PatientWaitingRoomRepository patientWaitingRoomRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PatientRepository patientRepository;


    /**
     * Count patient in a waiting room
     * @param waitingRoomId
     * @return number of patients in waiting room
     */
    public int countPatient(WaitingRoom waitingRoomId) {
        return patientWaitingRoomRepository.countByWaitingRoom(waitingRoomId);
    }

    /**
     * Change capacity
     * @param id
     * @param capacity
     * @return waiting room changed
     */
    public WaitingRoom updateWaitingRoom(int id, int capacity) {
        WaitingRoom waitingRoom = findWaitingRoomById(id);
        if (capacity < 1){
            return null;
        }
        waitingRoom.setCapacity(capacity);
        return waitingRoomRepository.save(waitingRoom);
    }

    /**
     * Get a waiting room by id
     * @param waitingRoomId
     * @return WaitingRoom
     */
    public WaitingRoom findWaitingRoomById(int waitingRoomId) {
        return waitingRoomRepository.findWaitingRoomByWaitingRoomID(waitingRoomId);
    }

    public Employee findByUsername(String username) {
        return employeeRepository.findByEmail(username);
    }

    /**
     * Get page waiting room with sorting
     * @param index
     * @param sortOption
     * @return a sorted page list of waiting rooms
     */
    public Page<WaitingRoomRequest> getAllWaitingRoomRequests(int index, String sortOption) {
        // Lấy toàn bộ phòng chờ từ cơ sở dữ liệu
        List<WaitingRoom> allWaitingRooms = waitingRoomRepository.findAll();
        List<WaitingRoomRequest> waitingRoomRequests = new ArrayList<>();

        // Chuyển đổi danh sách phòng chờ thành WaitingRoomRequest
        for (WaitingRoom waitingRoom : allWaitingRooms) {
            WaitingRoomRequest request = new WaitingRoomRequest();
            request.setWaitingRoomID(waitingRoom.getWaitingRoomID());
            request.setAvailable(countPatient(waitingRoom) < waitingRoom.getCapacity());
            request.setNumberPatient(countPatient(waitingRoom));
            request.setBranch(waitingRoom.getBranch());
            request.setCapacity(waitingRoom.getCapacity());
            waitingRoomRequests.add(request);
        }

        // Áp dụng sắp xếp
        getSortOption(sortOption, waitingRoomRequests);

        // Thiết lập phân trang thủ công
        Pageable pageable = PageRequest.of(index - 1, 2);
        int startIndex = (int) pageable.getOffset();
        int endIndex = Math.min(startIndex + pageable.getPageSize(), waitingRoomRequests.size());

        // Lấy danh sách phòng chờ đã phân trang
        List<WaitingRoomRequest> pagedRequests = waitingRoomRequests.subList(startIndex, endIndex);

        // Trả về đối tượng Page với kết quả đã phân trang
        return new PageImpl<>(pagedRequests, pageable, waitingRoomRequests.size());
    }


    private static void sortByBranchNameAscending(List<WaitingRoomRequest> waitingRoomRequests) {
        waitingRoomRequests.sort(Comparator.comparing(request -> request.getBranch().getBranchName()));
    }
    private static void sortByBranchNameDescending(List<WaitingRoomRequest> waitingRoomRequests) {
        waitingRoomRequests.sort(Comparator.comparing((WaitingRoomRequest request) ->
                request.getBranch().getBranchName()).reversed());
    }
    public static void sortByCapacityDescending(List<WaitingRoomRequest> waitingRoomRequests) {
        waitingRoomRequests.sort(Comparator.comparingInt(WaitingRoomRequest::getCapacity).reversed());
    }
    public static void sortByCapacityAscending(List<WaitingRoomRequest> waitingRoomRequests) {
        waitingRoomRequests.sort(Comparator.comparingInt(WaitingRoomRequest::getCapacity));
    }

    /**
     * Determine the sorting criteria based on the sort option
     * @param sortOption
     * @return Sort object for sorting the query
     */
    private static void getSortOption(String sortOption, List<WaitingRoomRequest> waitingRoomRequests) {
        switch (sortOption) {
            case "nameAsc":
                sortByBranchNameAscending(waitingRoomRequests);
                break;
            case "nameDesc":
                sortByBranchNameDescending(waitingRoomRequests);
                break;
            case "capacityAsc":
                sortByCapacityAscending(waitingRoomRequests);
                break;
            case "capacityDesc":
                sortByCapacityDescending(waitingRoomRequests);
                break;
            default:
                break;
        }
    }

    /**
     * Get page waiting room with search by name
     * @param index
     * @param keyword
     * @return a page list
     */
    public Page<WaitingRoomRequest> getSearchWaitingRoomRequests(int index, String keyword) {
        Pageable pageable = PageRequest.of(index - 1, 2);

        // Tìm danh sách phòng chờ theo keyword
        Page<WaitingRoom> waitingRoomsPage = waitingRoomRepository.findByNameContainingIgnoreCase(keyword, pageable);

        List<WaitingRoomRequest> waitingRoomRequests = new ArrayList<>();

        for (WaitingRoom waitingRoom : waitingRoomsPage) {
            WaitingRoomRequest request = new WaitingRoomRequest();
            request.setWaitingRoomID(waitingRoom.getWaitingRoomID());
            request.setAvailable(countPatient(waitingRoom) < waitingRoom.getCapacity());
            request.setNumberPatient(countPatient(waitingRoom));
            request.setBranch(waitingRoom.getBranch());
            request.setCapacity(waitingRoom.getCapacity());
            waitingRoomRequests.add(request);
        }

        return new PageImpl<>(waitingRoomRequests, pageable, waitingRoomsPage.getTotalElements());
    }

    /**
     * Get all patient waiting in room
     * @param index
     * @param waitingRoomId
     * @return list patient
     */
    public Page<PatientWaitingRoom> getAllPatientWaitingRequestsInRoom(int index, int waitingRoomId) {
        Pageable pageable = PageRequest.of(index - 1, 3);

        // Fetch all waiting requests for the waiting room
        List<PatientWaitingRoom> waitingRequests = patientWaitingRoomRepository.findByWaitingRoomId(waitingRoomId);

        // Sort the waiting requests: First by urgency (true first), then by booking status (true first)
        List<PatientWaitingRoom> sortedRequests = waitingRequests.stream()
                .sorted(Comparator.comparing(PatientWaitingRoom::isUrgency)
                        .thenComparing(PatientWaitingRoom::isBooked).reversed())
                .collect(Collectors.toList());

        // Use PageImpl to convert list to page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), sortedRequests.size());

        if (start > end) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return new PageImpl<>(sortedRequests.subList(start, end), pageable, sortedRequests.size());
    }

    /**
     * Search patients in waiting room by full name (first name and last name)
     * @param index
     * @param waitingRoomId
     * @param keyword
     * @return list of matching patients
     */
    public Page<PatientWaitingRoom> searchPatientsInWaitingRoom(int index, int waitingRoomId, String keyword) {
        Pageable pageable = PageRequest.of(index - 1, 3);

        // Lấy tất cả các yêu cầu đợi theo waitingRoomId
        List<PatientWaitingRoom> waitingRequests = patientWaitingRoomRepository.findByWaitingRoomId(waitingRoomId);

        // Lọc danh sách theo từ khóa (tìm trong firstName và lastName)
        List<PatientWaitingRoom> filteredRequests = waitingRequests.stream()
                .filter(request -> (request.getPatient().getFirstName() + " " + request.getPatient().getLastName()).toLowerCase().contains(keyword.toLowerCase()))
                .sorted(Comparator.comparing(PatientWaitingRoom::isUrgency)
                        .thenComparing(PatientWaitingRoom::isBooked).reversed())
                .collect(Collectors.toList());

        // Phân trang và chuyển đổi sang Page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredRequests.size());

        if (start > end) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return new PageImpl<>(filteredRequests.subList(start, end), pageable, filteredRequests.size());
    }

    /**
     * Find waiting room by branchId
     * @param branchId
     * @return waiting room
     */
    public WaitingRoom findWaitingRoomByBranchId(int branchId) {
        return waitingRoomRepository.findWaitingRoomByBranchID(branchId);
    }

    /**
     * Find an employee by email
     * @param email
     * @return employee
     */
    public Employee findByEmail(String email) {
        return employeeRepository.findByEmail(email);
    }

    //Get all Role
    public List<Role> getAllRole(){
        return roleRepository.findAll();
    }
    //Load all employee
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    //Find employee by Id
    public Employee getEmployeeById(int id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    //Change password employee
    public Employee changePassword(int id, EmployeeChangePasswordRequest employeeChangePasswordRequest) {
        Employee employee = getEmployeeById(id);

        if (!employeeChangePasswordRequest.getNewPassword().equals(employeeChangePasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and confirm password do not match");
        }
        employee.setPassword(employeeChangePasswordRequest.getNewPassword());
        return employeeRepository.save(employee);
    }
    
    //Update Employee
    public Employee updateEmployee(int id, EmployeeUpdateRequest employeeUpdateRequest) {
        Employee employee = getEmployeeById(id);

        if (employee == null) {
            throw new RuntimeException("Employee with id " + id + " not found.");
        }

        employee.setFirst_name(employeeUpdateRequest.getFirst_name());
        employee.setLast_name(employeeUpdateRequest.getLast_name());
        employee.setEmail(employeeUpdateRequest.getEmail());
        employee.setPhone(employeeUpdateRequest.getPhone());
        employee.setAddress(employeeUpdateRequest.getAddress());
        employee.setGender(employeeUpdateRequest.getGender());
        employee.setDob(employeeUpdateRequest.getDob());
        employee.setSalary(employeeUpdateRequest.getSalary());
        employee.setRole(employeeUpdateRequest.getRole());
        employee.setImg(employeeUpdateRequest.getImg());
        employee.set_active(employeeUpdateRequest.isActive());

        return employeeRepository.save(employee);
    }

    //Search account
    public List<Employee> searchEmployeesByNameOrPhone(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return employeeRepository.findAll(); // If don't enter keyword, return all list
        }
        return employeeRepository.findByNameContainingOrPhoneContaining(keyword);
    }
    //Search employee
    public List<Employee> searchAccountByNameOrId(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return employeeRepository.findAll(); // If don't enter keyword, return all list
        }
        return employeeRepository.findByNameContainingOrIdContaining(keyword);
    }


//Edit account
    public void updatePassword(int empId, String newPassword) {
        Employee employee = getEmployeeById(empId);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        employee.setPassword(encodedPassword);
        employeeRepository.save(employee);
    }

    /**
     * Add a branch to database
     * @param request a object
     * @return a ClinicBranchCreationRequest
     */
    public Branch createBranch(ClinicBranchCreationRequest request) {

        Branch newBranch = new Branch(); // create new branch and set information
        newBranch.setBranch_address(request.getBranch_address().trim());
        newBranch.setBranch_description(request.getBranch_description().trim());
        newBranch.setBranch_img(request.getBranch_img().trim());
        newBranch.setBranch_phone(request.getBranch_phone().trim());
        newBranch.setBranchName(request.getBranchName().trim());
        newBranch.setActive(true);

        return  branchRepository.save(newBranch); //save in database
    }

    /**
     * Create waiting room
     * @param waitingRoom
     * @param newBranch
     * @return waiting room created
     */
    public WaitingRoom createWaitingRoom(WaitingRoom waitingRoom, Branch newBranch){
        WaitingRoom newWaitingRoom = new WaitingRoom();
        newWaitingRoom.setBranch(newBranch);
        newWaitingRoom.setCapacity(10);
        newWaitingRoom.setAvailable(true);

        return waitingRoomRepository.save(newWaitingRoom);
    }

    /**
     * Get all branch
     * @return a list <code>java.util.List</code>
     */
    public List<Branch> getAllBranches() {
        return branchRepository.findAll(); //Return list of branch
    }

    public Page<Branch> getAllBranchesPage(Pageable pageable) {
        return branchRepository.findAll(pageable); // Trả về danh sách phân trang
    }


    /**
     * Get a branch by Id
     * @param id
     * @return branch, a branch have id
     */
    public Branch getBranchById(int id) {
        return branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid Branch ID")); //return id or exception
    }

    /**
     * Update branch in database
     * @param id
     * @param updateBranchRequest
     * @return updated branch have id
     */
    public Branch updateBranch(int id, ClinicBranchUpdateRequest updateBranchRequest) {
        Branch branch = getBranchById(id);//Find the branch to modify

        if (branch == null) { //If null throw run time exception
            throw new RuntimeException("Branch with id " + id + " not found.");
        }

        //Set information need to modify
        branch.setBranch_address(updateBranchRequest.getBranch_address().trim());
        branch.setBranch_description(updateBranchRequest.getBranch_description().trim());
        branch.setBranch_img(updateBranchRequest.getBranch_img().trim());
        branch.setBranch_phone(updateBranchRequest.getBranch_phone().trim());
        branch.setBranchName(updateBranchRequest.getBranchName().trim());
        return branchRepository.save(branch); // Return updated branch
    }

    //Delete Branch
    public void deleteBranch(int id) {
        branchRepository.deleteById(id);
    }

    //Tìm tất cả tài khoản chưa hoạt động
    public List<Employee> findAllInactiveAccount() {
        List<Employee> inactiveAccountList = new ArrayList<>();
        List<Employee> allAccount = employeeRepository.findAll();
        for (Employee employee : allAccount) {
            if(!employee.is_active()) {
                inactiveAccountList.add(employee);
            }
        }
        return inactiveAccountList;
    }

    //Seacrh các tài khoản chưa được phê duyệt
    public List<Employee> searchInactiveAccount(String keyword) {
        return employeeRepository.searchInactiveEmployee(keyword);
    }

    //Phan trang seacrh các tài khoản chưa được phê duyệt
    public Page<Employee> searchPageInactiveAccount(String keyword, int pageNo) {

        Pageable pageable = PageRequest.of(pageNo - 1, 4);
        return employeeRepository.searchPageInactiveEmployee(keyword, pageable);
    }

    //Accept tài khoản
    public void acceptAccount(List<Integer> empIdList) {
        for (Integer empId : empIdList) {
            Employee employee = getEmployeeById(empId);
            if (employee == null) {
                throw new RuntimeException("Employee with id " + empId + " not found.");
            }
            employee.set_active(true);
            employeeRepository.save(employee);
        }
    }

    //Reject tài khoản
    public void rejectAccount(List<Integer> empIdList) {
        for (Integer empId : empIdList) {
            Employee employee = getEmployeeById(empId);
            if (employee == null) {
                throw new RuntimeException("Employee with id " + empId + " not found.");
            }
            employeeRepository.delete(employee);
        }
    }

    //Get All Patient
    public List<Patient> getAllPatient(){
        return patientRepository.findAll();
    }

    public boolean deletePatientById(int id){
        try{
            patientRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Page<Patient> getPatientPaging(int page, int size){
        return patientRepository.findAll(PageRequest.of(page, size));
    }

    //Create new Patient
    public Patient createPatient(PatientCreationRequest request) {
        Patient newPatient = new Patient();
        newPatient.setFirstName(request.getFirstName());
        newPatient.setLastName(request.getLastName());
        newPatient.setEmail(request.getEmail());
        newPatient.setPhone(request.getPhone());
        newPatient.setAddress(request.getAddress());
        newPatient.setGender(request.getGender());
        newPatient.setDob(request.getDob());
        return patientRepository.save(newPatient);
    }

    //Get Patient By ID
    public Patient getPatient(int id){
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid Patient ID"));
    }

    //Update Patient
    public void updatePatient(int id, PatientUpdateRequest updateRequest) {
        Patient patient = getPatient(id);

        if (patient == null) {
            throw new RuntimeException("Patient with id " + id + " not found.");
        }
        patient.setFirstName(updateRequest.getFirstName().trim());
        patient.setLastName(updateRequest.getLastName().trim());
        patient.setEmail(updateRequest.getEmail().trim());
        patient.setPhone(updateRequest.getPhone().trim());
        patient.setAddress(updateRequest.getAddress().trim());
        patient.setGender(updateRequest.getGender().trim());
        patient.setDob(updateRequest.getDob());
        patient.setMedicalHistory(updateRequest.getMedicalHistory());
        patientRepository.save(patient);
    }

    /**
     * Add patient waiting room
     * @param patientWaitingRoom
     * @return patient added
     */
    public PatientWaitingRoom addPatientWaitingRoom(PatientWaitingRoomRequest patientWaitingRoom) {
        patientWaitingRoom.setStatus("Waiting");
        patientWaitingRoom.setWaitingDate(LocalDate.now());

        PatientWaitingRoom newPatientWaitingRoom = new PatientWaitingRoom();
        newPatientWaitingRoom.setPatient(patientWaitingRoom.getPatient());
        newPatientWaitingRoom.setWaitingDate(patientWaitingRoom.getWaitingDate());
        newPatientWaitingRoom.setStatus(patientWaitingRoom.getStatus());
        newPatientWaitingRoom.setBooked(patientWaitingRoom.isBooked());
        newPatientWaitingRoom.setNote(patientWaitingRoom.getNote());
        newPatientWaitingRoom.setUrgency(patientWaitingRoom.isUrgency());
        newPatientWaitingRoom.setWaitingRoomId(patientWaitingRoom.getWaitingRoom());
        return patientWaitingRoomRepository.save(newPatientWaitingRoom);
    }

    public Patient findPatientById(int id) {
        return patientRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid Patient ID"));
    }

    //Phan trang manage register account
    public Page<Employee> getAllPageInActiveAccount(int pageNo){
        Pageable pageable = PageRequest.of(pageNo - 1, 4);
        return employeeRepository.findAllInactive(pageable);
    }

}
