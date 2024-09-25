package Project.SWP391_2024_Fall_SE1866_Group1.Service;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.*;
import Project.SWP391_2024_Fall_SE1866_Group1.Repository.*;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.DoctorCreationRequest;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ReceptionistCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DoctorService {

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
    public void createDoctor(DoctorCreationRequest request) {
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
        employee.set_accept(request.isAccept());
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

        Doctor doctor = new Doctor();
        doctor.setDoctor_img(request.getDoctor_img());
        doctor.setDoctor_description(request.getDoctor_description());
        doctor.setDoctorCertification(request.getDoctorCertification());
        doctor.setDoctorSpecification(request.getDoctorSpecification());
        doctor.setEmployee(employee);
        employee.setDoctor(doctor);
        doctorRepository.save(doctor);
        employeeRepository.save(employee);
    }

    public Role findRoleById(int id) {
        return roleRepository.findById(id);
    }

    public Role findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public List<Branch> findAllBranches() {
        return branchRepository.findAll();
    }

    public Doctor findDoctorById(int id) {
        return doctorRepository.findById(id);
    }

}
