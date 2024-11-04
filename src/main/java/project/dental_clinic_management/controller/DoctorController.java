package project.dental_clinic_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.PatientWaitingRoom;
import project.dental_clinic_management.entity.WaitingRoom;
import project.dental_clinic_management.service.AdminService;
import project.dental_clinic_management.service.ReceptionistService;

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
            // Search for patients by name in the waiting room
            patientWaitingRequests = receptionistService.searchPatientsInWaitingRoomByName(page, waitingRoomId, searchQuery);
            model.addAttribute("searchQuery", searchQuery);
        } else {
            // Retrieve all patients in the waiting room with pagination
            patientWaitingRequests = receptionistService.getAllPatientWaitingRequestsInRoomIsWaiting(page, waitingRoomId);
        }

        // Add attributes to the model for view rendering
        model.addAttribute("listPatient", patientWaitingRequests.getContent());
        model.addAttribute("waitingRoomId", waitingRoomId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", patientWaitingRequests.getTotalPages());
        model.addAttribute("hasPrevious", patientWaitingRequests.hasPrevious());
        model.addAttribute("hasNext", patientWaitingRequests.hasNext());

        return "/employee/doctorTakePatient"; // Update this path as necessary for your project structure
    }

}
