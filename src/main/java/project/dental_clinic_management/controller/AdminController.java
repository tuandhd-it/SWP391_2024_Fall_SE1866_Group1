package project.dental_clinic_management.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.dental_clinic_management.dto.request.*;
import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.entity.Branch;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.Role;
import project.dental_clinic_management.service.AdminService;
import project.dental_clinic_management.dto.request.ClinicBranchCreationRequest;
import project.dental_clinic_management.dto.request.ClinicBranchUpdateRequest;
import project.dental_clinic_management.dto.request.EmployeeChangePasswordRequest;
import project.dental_clinic_management.dto.request.EmployeeUpdateRequest;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.dental_clinic_management.service.ServiceService;
import project.dental_clinic_management.service.TimeTrackingService;

import java.time.LocalDate;
import java.util.List;

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
     * @param model
     * @return string url
     */
    @GetMapping("/listWaitingRoom")
    public String listWaitingRoom(@RequestParam(defaultValue = "1") int page, Model model) {

        Page<WaitingRoomRequest> waitingRoomPage = adminService.getAllWaitingRoomRequests(page);

        model.addAttribute("listRoom", waitingRoomPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", waitingRoomPage.getTotalPages());
        return "/branch/listWaitingRoom";
    }

    /**
     * Get a list of branchs and send models to specified page
     * @param model, it is <code>org.springframework.ui.Model</code>
     * @return a url <code>java.lang.String</code>
     */
    @GetMapping("/manageBranchs")
    public String getAllBranches(Model model) {
        //Get list Branch
        List<Branch> list = adminService.getAllBranches();
        model.addAttribute("branches", list);//Add in model
        ClinicBranchUpdateRequest updateRequest = new ClinicBranchUpdateRequest(); // Create a object to update branch
        model.addAttribute("updateBranch", updateRequest); //Add in model
        ClinicBranchCreationRequest creationRequest = new ClinicBranchCreationRequest();// Create a object to create branch
        model.addAttribute("createBranch", creationRequest); //Add in model
        return "/branch/manageBranch";
    }
    /**
     * Get a list of branchs and send models to specified page
     * @param model, it is <code>org.springframework.ui.Model</code>
     * @return a url <code>java.lang.String</code>
     */
    @GetMapping("/manageMedicine")
    public String getAllMedicines(Model model) {
        List<Medicine> list = adminService.getAllMedicines();
        model.addAttribute("medicines", list);//Add in model
        return "/medicine/manageMedicine";
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
    public String getAllPatient(Model model) {
        List<Patient> list = adminService.getAllPatient();
        model.addAttribute("patients", list);
        model.addAttribute("editPatient",new PatientUpdateRequest());
        model.addAttribute("newPatient", new PatientCreationRequest());
        return "/patient/managePatient";
    }

    @PostMapping("/patientCreate")
    public String createPatient(@ModelAttribute PatientCreationRequest patientRequest) {
        adminService.createPatient(patientRequest); //Create branch
        return "redirect:/admin/managePatient";
    }

    @PostMapping("/editPatient")
    public String editPatient(@ModelAttribute PatientUpdateRequest patientRequest) {
        adminService.updatePatient(patientRequest.getPatientId(), patientRequest); // Update branch
        return "redirect:/admin/managePatient";
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
    public String acceptAccount(@RequestParam List<Integer> emp_id, Model model) {
        adminService.acceptAccount(emp_id);
        List<Employee> list = adminService.findAllInactiveAccount();
        model.addAttribute("employees", list);
        model.addAttribute("acceptMsg", "Accounts accepted!");
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
