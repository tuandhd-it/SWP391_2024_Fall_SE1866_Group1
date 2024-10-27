package project.dental_clinic_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import project.dental_clinic_management.dto.request.*;
import project.dental_clinic_management.entity.*;
import project.dental_clinic_management.repository.*;

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


}
