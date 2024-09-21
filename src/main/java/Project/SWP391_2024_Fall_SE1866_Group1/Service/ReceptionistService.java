package Project.SWP391_2024_Fall_SE1866_Group1.Service;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Account;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Branch;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Employee;
import Project.SWP391_2024_Fall_SE1866_Group1.Repository.*;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ReceptionistCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ReceptionistService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    //Create a new receptionist
    public void createReceptionist(ReceptionistCreationRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Employee employee = new Employee();
        Account account = new Account();

        //Create branch to store in receptionist information
        Branch branch = branchRepository.findByName(request.getBranch_name());

        //Create account to store in receptionist information
        String password = encoder.encode(request.getPassword());
        account.setPassword(password);
        account.setUsername(request.getUsername());
        accountRepository.save(account);
        employee.setFirst_name(request.getFirst_name());
        employee.setLast_name(request.getLast_name());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setAddress(request.getAddress());
        employee.setSalary(request.getSalary());
        employee.setStatus(request.getStatus());
        employee.setDoctor(null);
        employee.setBranch(branch);
        employee.setRole(request.getRole());
        employeeRepository.save(employee);
    }


}
