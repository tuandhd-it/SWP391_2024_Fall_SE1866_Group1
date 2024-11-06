package project.dental_clinic_management.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.dental_clinic_management.dto.request.*;
import project.dental_clinic_management.entity.Record;
import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.service.RecordService;
import project.dental_clinic_management.service.*;

import java.time.LocalDate;
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
@RequestMapping("/admin")

/**
 * The class contain method to navigate to show information
 * Staff table in database. In the update or insert method, all data will be normalized (trim space) before update/insert into database
 * The methods will navigate to a error page if it have error
 *
 * @author: Nguyen Viet Lam
 */
public class AdminController {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ServiceService serviceService;
    @Autowired
    private TimeTrackingService timeTrackingService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private ReceptionistService receptionistService;



    /**
     * Get data and bring it to page
     * @param waitingRoomId
     * @param page
     * @param model
     * @return path to page
     */
    @GetMapping("/patientList/{id}/patients")
    public String patientList(@PathVariable("id") int waitingRoomId,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              Model model) {
        // Fetch the list of patients for the given waiting room ID and page index
        Page<PatientWaitingRoom> patientWaitingRequests = adminService.getAllPatientWaitingRequestsInRoom(page, waitingRoomId);
        // Add the list of patients and other necessary data to the model
        model.addAttribute("listPatient", patientWaitingRequests.getContent());
        model.addAttribute("waitingRoomId", waitingRoomId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", patientWaitingRequests.getTotalPages());
        model.addAttribute("hasPrevious", patientWaitingRequests.hasPrevious()); // Check if there is a previous page
        model.addAttribute("hasNext", patientWaitingRequests.hasNext()); // Check if there is a next page

        // Return the view name that will display the patient list
        return "/branch/listPatientsWaiting"; // Ensure this path corresponds to your directory structure
    }

    /**
     * Search patient
     * @param waitingRoomId
     * @param searchQuery
     * @param page
     * @param model
     * @return path to page
     */
    @GetMapping("/searchPatient/{id}/patients")
    public String searchPatientList(@PathVariable("id") int waitingRoomId,
                                    @RequestParam("searchQuery") String searchQuery,
                                    @RequestParam(value = "page", defaultValue = "1") int page,
                                    Model model) {
        // Sử dụng searchQuery để tìm kiếm bệnh nhân trong phòng chờ
        Page<PatientWaitingRoom> patientWaitingRequests = adminService.searchPatientsInWaitingRoom(page, waitingRoomId, searchQuery);

        // Thêm dữ liệu vào model để truyền sang view
        model.addAttribute("listPatient", patientWaitingRequests.getContent());
        model.addAttribute("waitingRoomId", waitingRoomId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", patientWaitingRequests.getTotalPages());
        model.addAttribute("hasPrevious", patientWaitingRequests.hasPrevious());
        model.addAttribute("hasNext", patientWaitingRequests.hasNext());
        model.addAttribute("searchQuery", searchQuery);

        // Trả về view hiển thị danh sách bệnh nhân
        return "/branch/listPatientsWaiting";
    }




    /**
     * Change capacity in database
     * @param roomID
     * @param capacity
     * @return path to page
     */
    @PostMapping("/updateCapacity")
    public String updateCapacity(@RequestParam("roomID") String roomID,
            @RequestParam("capacity") String capacity) {

        try {
            if(roomID != null && capacity != null) {
                int roomId = Integer.parseInt(roomID.trim());
                int capacityInt = Integer.parseInt(capacity.trim());
                adminService.updateWaitingRoom(roomId, capacityInt);
            }
        } catch (Exception e) {
            return "redirect:/admin/listWaitingRoom";
        }
        // Cập nhật số lượng bệnh nhân tối đa cho phòng chờ


        // Chuyển hướng về trang danh sách phòng chờ (hoặc ở lại trang hiện tại)
        return "redirect:/admin/listWaitingRoom";
    }

    /**
     * Create a branch and add it in database and redirect to specified page
     * @param branchRequest
     * @return a url <code>java.lang.String</code>
     */
    @PostMapping("/createBranch")
    public String createBranch(@ModelAttribute ClinicBranchCreationRequest branchRequest) {
        Branch branch = adminService.createBranch(branchRequest); //Create branch
        WaitingRoom newWaitingRoom = new WaitingRoom();
        newWaitingRoom.setBranch(branch);
        newWaitingRoom.setCapacity(10);
        newWaitingRoom.setAvailable(true);
        adminService.createWaitingRoom(newWaitingRoom, branch);
        return "redirect:/admin/manageBranchs";
    }

    /**
     * Lead to page need
     * @param page
     * @param sortOption
     * @param model
     * @return string url
     */
    @GetMapping("/listWaitingRoom")
    public String listWaitingRoom(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(name = "sort" , defaultValue = "default") String sortOption,
            Model model) {

        // Gọi hàm service với tham số sortOption
        Page<WaitingRoomRequest> waitingRoomPage = adminService.getAllWaitingRoomRequests(page, sortOption);

        // Đưa danh sách phòng chờ và các thông tin phân trang vào model
        model.addAttribute("listRoom", waitingRoomPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", waitingRoomPage.getTotalPages());
        model.addAttribute("sortOption", sortOption); // Gán sortOption để giữ trạng thái sắp xếp hiện tại trong view

        return "/branch/listWaitingRoom";
    }

    /**
     * Lead to the page with the search results for waiting rooms
     * @param page The page number to display
     * @param keyword The search keyword for the waiting room's name
     * @param model The model to store attributes for the view
     * @return The URL of the waiting room list page
     */
    @GetMapping("/searchWaitingRoom")
    public String searchWaitingRoom(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        Page<WaitingRoomRequest> waitingRoomPage = adminService.getSearchWaitingRoomRequests(page, keyword);

        model.addAttribute("listRoom", waitingRoomPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", waitingRoomPage.getTotalPages());
        model.addAttribute("keyword", keyword);

        return "/branch/listWaitingRoom";
    }

    @GetMapping("/manageBranchs")
    public String getAllBranches(Model model,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "2") int size) {
        // Phân trang danh sách cơ sở
        Pageable pageable = PageRequest.of(page, size);
        Page<Branch> branchPage = adminService.getAllBranchesPage(pageable);

        model.addAttribute("branches", branchPage.getContent()); // Lấy danh sách cơ sở cho trang hiện tại
        model.addAttribute("totalPages", branchPage.getTotalPages()); // Tổng số trang
        model.addAttribute("currentPage", page); // Trang hiện tại

        // Đối tượng update và create
        model.addAttribute("updateBranch", new ClinicBranchUpdateRequest());
        model.addAttribute("createBranch", new ClinicBranchCreationRequest());

        return "/branch/manageBranch";
    }


    /**
     * Edit the specified branch and navigate to
     * @param branchRequest
     * @return a url <code>java.lang.String</code>
     */
    @PostMapping("/editBranch")
    public String editBranch(@ModelAttribute ClinicBranchUpdateRequest branchRequest) {
        adminService.updateBranch(branchRequest.getId(), branchRequest); // Update branch
        return "redirect:/admin/manageBranchs";
    }

    @GetMapping("/manageAcc")
    public String getAllEmployees(Model model) {
        List<Employee> list = adminService.getAllEmployees();
        model.addAttribute("employees", list);
        return "/employee/manageAcc";
    }

    @GetMapping("/manageEmp")
    public String getAllEmployeesInfo(Model model) {
        List<Employee> list = adminService.getAllEmployees();
        model.addAttribute("employees", list);
        return "/employee/manageEmp";
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
        model.addAttribute("editPatient", new PatientUpdateRequest());
        model.addAttribute("newPatient", new PatientCreationRequest());
        model.addAttribute("totalPatient", list.getTotalElements());
        model.addAttribute("start", page * size + 1);
        model.addAttribute("end", Math.min((page + 1) * size, (int) list.getTotalElements()));

        return "/patient/managePatient";
    }

    @PostMapping("/patientCreate")
    public String createPatient(@ModelAttribute @Valid @RequestBody PatientCreationRequest patientRequest, BindingResult bindingResult, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            bindingResult.getFieldErrors().forEach(
                    error -> errors.put(error.getField(), error.getDefaultMessage())
            );

            StringBuilder errorMsg = new StringBuilder();
            errorMsg.append("Tạo mới bệnh nhân thất bại").append("<br>");

            for (String key : errors.keySet()) {
                errorMsg.append(key).append(": ").append(errors.get(key)).append("<br>");;
            }
            model.addAttribute("errors", errorMsg);
            int page=0,size=5;
            Page<Patient> list = adminService.getPatientPaging(page,size);
            model.addAttribute("patients", list);
            model.addAttribute("editPatient",new PatientUpdateRequest());
            model.addAttribute("newPatient", patientRequest);
            model.addAttribute("totalPatient", list.getTotalElements());
            model.addAttribute("start", 1);
            model.addAttribute("end", Math.min((page + 1) * size, (int)list.getTotalElements()));
            return "patient/managePatient";
        }else{
            Patient patient = adminService.createPatient(patientRequest);
            model.addAttribute("errors", "Thêm mới Bệnh Nhân Thành Công!");
            PatientWaitingRoomRequest patientWaitingRoom = new PatientWaitingRoomRequest();
            patientWaitingRoom.setPatient(patient);
            model.addAttribute("patientWaitingRoom", patientWaitingRoom);
            return "/branch/addPatientWaitingRoom";
        }
    }

    @PostMapping("/addPatientWaitingRoom")
    public String addPatientToWaitingRoom(@ModelAttribute @Valid @RequestBody PatientWaitingRoomRequest patientWaitingRoom,
                                          @RequestParam(name = "book", defaultValue = "false") String book,
                                          @RequestParam(name = "urgent", defaultValue = "false") String urgent,
                                          @RequestParam(name = "patientId") String patientId,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        // Lưu thông tin bệnh nhân vào phòng chờ
        boolean booked = false;
        boolean urgented = false;
        int patientid;
        try {
            if (book.equals("true")) {
                booked = true;
            }
            if (urgent.equals("true")) {
                urgented = true;
            }
            patientid = Integer.parseInt(patientId);
            Patient patient = adminService.findPatientById(patientid);
            patientWaitingRoom.setPatient(patient);
            patientWaitingRoom.setBooked(booked);
            patientWaitingRoom.setUrgency(urgented);
            String username = userDetails.getUsername();
            Employee receptionist = adminService.findByUsername(username);
            patientWaitingRoom.setWaitingRoom(adminService.findWaitingRoomByBranchId(receptionist.getBranch().getBran_id()));
            adminService.addPatientWaitingRoom(patientWaitingRoom);
        } catch (Exception e){

        }

        // Điều hướng lại trang hiển thị danh sách bệnh nhân trong phòng chờ hoặc trang khác
        return "redirect:/admin/listWaitingRoom"; // Cập nhật URL theo cấu trúc của bạn
    }

    @PostMapping("/editPatient")
    public String editPatient(
            @ModelAttribute @Valid PatientUpdateRequest patientRequest,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(
                    error -> errors.put(error.getField(), error.getDefaultMessage())
            );

            StringBuilder errorMsg = new StringBuilder();
            errorMsg.append("Cập nhật bệnh nhân thất bại").append("<br>");

            for (String key : errors.keySet()) {
                errorMsg.append(key).append(": ").append(errors.get(key)).append("<br>");
            }

            redirectAttributes.addFlashAttribute("errors", errorMsg.toString());
        } else {
            redirectAttributes.addFlashAttribute("errors", "Cập nhật Bệnh Nhân Thành Công!");
            adminService.updatePatient(patientRequest.getPatientId(), patientRequest);
        }

        return "redirect:/admin/managePatient";
    }


    @GetMapping("/deletePatient/{id}")
    public String deletePatient(@PathVariable Integer id,
                                RedirectAttributes redirectAttributes) {
        List<Record> records = recordService.getAllRecordsByPatientID(id);
        if(records.isEmpty()){
            adminService.deletePatientById(id);
            redirectAttributes.addFlashAttribute("errors", "Xóa bệnh nhân thành công!");
        }else{
            redirectAttributes.addFlashAttribute("errors", "Thất bại!<br>Không thể xóa bệnh nhân đã có lịch sử thăm khám!");
        }
        return "redirect:/admin/managePatient";
    }

    @GetMapping("/listRecord/{id}")
    public String getRecordOfPatient(@PathVariable Integer id,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     Model model) {
        Page<Record> records = recordService.getAllRecordsByPatientID(id,page,size);
        model.addAttribute("records",records);
        model.addAttribute("totalRecord", records.getTotalElements());
        model.addAttribute("start", page * size + 1);
        model.addAttribute("end", Math.min((page + 1) * size, (int)records.getTotalElements()));
        return "/patient/manageRecord";
    }


    @GetMapping("employeesDetails/{id}")
    public String showEmployeeDetails(@PathVariable("id") Integer empId, Model model) {
        List<Branch> listBranch = adminService.getAllBranches();
        List<Role> listRole = adminService.getAllRole();

        //Get Employee to fill data to EmployeeRequest
        Employee employee = adminService.getEmployeeById(empId);
        EmployeeUpdateRequest employeeUpdateRequest = new EmployeeUpdateRequest();
        employeeUpdateRequest.setEmp_id(empId);
        employeeUpdateRequest.setFirst_name(employee.getFirst_name());
        employeeUpdateRequest.setLast_name(employee.getLast_name());
        employeeUpdateRequest.setEmail(employee.getEmail());
        employeeUpdateRequest.setPhone(employee.getPhone());
        employeeUpdateRequest.setDob(employee.getDob());
        employeeUpdateRequest.setGender(employee.getGender());
        employeeUpdateRequest.setAddress(employee.getAddress());
        employeeUpdateRequest.setImg(employee.getImg());
        employeeUpdateRequest.setActive(employee.is_active());
        employeeUpdateRequest.setSalary(employee.getSalary());
        employeeUpdateRequest.setBranch_id(employee.getBranch().getBran_id());
        employeeUpdateRequest.setDescription(employee.getDescription());
        employeeUpdateRequest.setRole(employee.getRole());
        model.addAttribute("employee", employee);
        model.addAttribute("listBranches", listBranch);
        model.addAttribute("listRoles", listRole);
        model.addAttribute("editEmployees", employeeUpdateRequest);
        return "/employee/detailsEmp";
    }



    @PutMapping("/changeEmployeePass")
    public String changeEmployeePass(@ModelAttribute EmployeeChangePasswordRequest employeeRequest,  RedirectAttributes redirectAttributes) {
        adminService.changePassword(employeeRequest.getEmp_id(), employeeRequest);
        redirectAttributes.addFlashAttribute("message", "Password changed successfully!");
        return "redirect:/profile";
    }

    @PostMapping("/updateEmployee")
    public String updateEmployee(@ModelAttribute EmployeeUpdateRequest employeeRequest, RedirectAttributes redirectAttributes) {
        adminService.updateEmployee(employeeRequest.getEmp_id(), employeeRequest);
        redirectAttributes.addFlashAttribute("message", "Employee updated successfully!");
        return "redirect:/profile";
    }

    // Mapping for searching employees
    @GetMapping("/searchAccount")
    public String searchEmployees(@RequestParam("keyword") String keyword, Model model) {
        // Call the service to search
        List<Employee> employees = adminService.searchEmployeesByNameOrPhone(keyword);
        // Add the list of employees to the model for the view
        model.addAttribute("employees", employees);
        // Add the keyword to the model to keep the search term in the view's input field
        model.addAttribute("keyword", keyword);
        // Return the view for managing employee accounts
        return "/employee/manageAcc";
    }

    // Mapping for searching employee accounts
    @GetMapping("/searchEmployees")
    public String searchAccount(@RequestParam("keyword") String keyword, Model model) {
        // Call the service to search
        List<Employee> employees = adminService.searchAccountByNameOrId(keyword);
        // Add the list of employees to the model for the view
        model.addAttribute("employees", employees);
        // Add the keyword to the model to keep the search term in the view's input field
        model.addAttribute("keyword", keyword); // Retain the keyword in the search box
        // Return the view for displaying the list of employees
        return "/employee/manageEmp";
    }



    // Mapping for editing employee's password
    @GetMapping("/editEmployee/{id}")
    public String editEmployee(@PathVariable("id") int id, Model model) {
        // Retrieve employee by id using admin service
        Employee employee = adminService.getEmployeeById(id);
        // Add the retrieved employee object to the model
        model.addAttribute("employee", employee);
        // Return the view for editing employee password
        return "editEmployeePassword";
    }

    // Mapping for updating employee's password
    @PostMapping("/updatePassword")
    public String updatePassword(@RequestParam("emp_id") int empId, @RequestParam("password") String newPassword, RedirectAttributes redirectAttributes) {
        // Update the employee's password using admin service
        adminService.updatePassword(empId, newPassword);
        // Add a flash attribute with a success message to be shown after redirect
        redirectAttributes.addFlashAttribute("message", "Password updated successfully!");
        // Redirect to the manage accounts page after password update
        return "redirect:/admin/manageAcc";
    }

    //Hiển thị tài khoản được đăng ký cần xét duyệt
    @GetMapping("/manageRegisterAccount")
    public String manageRegisterAccount(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Employee> list = adminService.findAllInactiveAccount();
        model.addAttribute("employees", list);
        return "/employee/manageRegisterAccount";
    }

    //Hiển thị danh sách các tài khoản chưa được cấp phép sử dụng hệ thống
    @GetMapping("/searchInactiveAccount")
    public String searchInactiveAccount(@RequestParam("keyword") String keyword, Model model) {

        List<Employee> employees = adminService.searchInactiveAccount(keyword);

        model.addAttribute("employees", employees);

        model.addAttribute("keyword", keyword);

        return "/employee/manageRegisterAccount";
    }

    //Phê duyệt tài khoản
    @PostMapping("/acceptAccount")
    public String acceptAccount(@RequestParam List<Integer> emp_id, Model model, @RequestParam("action") String action) {
        if(action.equalsIgnoreCase("accept")) {
            adminService.acceptAccount(emp_id);
            model.addAttribute("acceptMsg", "Accounts accepted!");
        } else if (action.equalsIgnoreCase("reject")) {
            adminService.rejectAccount(emp_id);
            model.addAttribute("rejectMsg", "Accounts rejected!");
        }
        List<Employee> list = adminService.findAllInactiveAccount();
        model.addAttribute("employees", list);

        return "/employee/manageRegisterAccount";
    }
    @GetMapping("/manageService")
    public String serviceList(Model model,@RequestParam(value = "page", defaultValue = "1")int page,
     @RequestParam(value = "search", required = false,defaultValue = "") String search,
     @RequestParam(value = "sort", required = false ,defaultValue = "idAsc") String sort,
     @RequestParam(value = "searchType", defaultValue = "name",required = false) String type)
    {
        page = page-1;
        Page<Service> listPage = serviceService.getAllByPage(page,sort,search,type);

        model.addAttribute("listService", listPage.getContent());
        model.addAttribute("totalPages", listPage.getTotalPages());
        model.addAttribute("currentPage", page+1);
        model.addAttribute("search", search);
        model.addAttribute("sort", sort);
        model.addAttribute("searchType", type);
        return "/service/manageService";
    }

    @GetMapping("/detail/{id}")
    public String serviceList(Model model,@PathVariable("id")int id)
    {
        Service service = serviceService.getServiceById(id);
        model.addAttribute("service", service);
        return "/service/detailService";
    }
    @GetMapping("/tracking")
    public String viewAttendance(@RequestParam(value = "date", required = false) LocalDate date, Model model) {
        // Nếu không có ngày được yêu cầu, sử dụng ngày hôm nay
        if (date == null) {
            date = LocalDate.now();
        }
        List<TimeTracking> attendanceList = timeTrackingService.findByDate(date);
        model.addAttribute("attendances", attendanceList);
        model.addAttribute("selectedDate", date);
        return "timeTracking/managerTimeTracking";
    }
    @GetMapping("/updateService/{id}")
    public String updateServiceGet(@PathVariable("id")int id, Model model) {
        Service service = serviceService.getServiceById(id);
        model.addAttribute("service", service);
        return "service/editService"; // Redirect back to the service list
    }



}
