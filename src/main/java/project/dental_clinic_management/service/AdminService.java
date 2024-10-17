package project.dental_clinic_management.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import project.dental_clinic_management.dto.request.*;
import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    private MedicineRepository medicineRepository;



    /**
     * Get waiting room list
     * @return waiting room list
     */
    public List<WaitingRoom> getAllWaitingRooms() {
        return waitingRoomRepository.findAll(); //return  waiting room list
    }

    /**
     * Count patient in a waiting room
     * @param waitingRoomId
     * @return number of patients in waiting room
     */
    public int countPatient(WaitingRoom waitingRoomId) {
        return patientWaitingRoomRepository.countByWaitingRoomId(waitingRoomId);
    }

    /**
     * Change capacity
     * @param id
     * @param capacity
     * @return waiting room changed
     */
    public WaitingRoom updateWaitingRoom(int id, int capacity) {
        WaitingRoom waitingRoom = findWaitingRoomById(id);
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


    /**
     * Change capacity of waiting room
     * @param waitingRoomID
     * @param newCapacity
     * @return
     */
    public WaitingRoom editCapacityOfWaitingRoom(int waitingRoomID, int newCapacity) {
        WaitingRoom waitingRoom = findWaitingRoomById(waitingRoomID);
        waitingRoom.setCapacity(newCapacity);
        return waitingRoomRepository.save(waitingRoom);
    }

    /**
     * Get page waiting room
     * @param index
     * @return a page list
     */
    public Page<WaitingRoomRequest> getAllWaitingRoomRequests(int index){
        Pageable pageable = PageRequest.of(index - 1,2);
        Page<WaitingRoom> waitingRoomsPage = waitingRoomRepository.findAll(pageable); // Lấy danh sách phòng chờ theo trang
        List<WaitingRoomRequest> waitingRoomRequests = new ArrayList<>(); // Tạo danh sách WaitingRoomRequest

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
    public Page<PatientWaitingRoom> getAllPatientWaitingRequestsInRoom(int index, int waitingRoomId){
        Pageable pageable = PageRequest.of(index - 1,3);
        // Find the waiting room by ID

        // Fetch all waiting requests for the waiting room
        List<PatientWaitingRoom> waitingRequests = patientWaitingRoomRepository.findByWaitingRoomId(waitingRoomId);

        // Use PageImpl to convert list to page
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), waitingRequests.size());

        if (start > end) {
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        return new PageImpl<>(waitingRequests.subList(start, end), pageable, waitingRequests.size());
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

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }
}
