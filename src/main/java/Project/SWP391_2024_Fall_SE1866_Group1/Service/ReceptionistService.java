package Project.SWP391_2024_Fall_SE1866_Group1.Service;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Branch;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.CustomEmployeeDetails;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Employee;
import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Role;
import Project.SWP391_2024_Fall_SE1866_Group1.Repository.*;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ReceptionistCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;

@Service
public class ReceptionistService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RoleRepository roleRepository;


    public Employee findByUsername(String username) {
        return employeeRepository.findByEmail(username);
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
        employee.set_active(request.is_active());
        employee.set_accept(request.is_accept());
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

    public CustomEmployeeDetails findAccountByUsername(String username) {
        Employee employee = employeeRepository.findByEmail(username);
        Collection<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(employee.getRole().getRoleName()));
        CustomEmployeeDetails details = new CustomEmployeeDetails(employee, authorities);

        return details;
    }

    public Role findRoleById(int id) {
        return roleRepository.findById(id);
    }

    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }


}
