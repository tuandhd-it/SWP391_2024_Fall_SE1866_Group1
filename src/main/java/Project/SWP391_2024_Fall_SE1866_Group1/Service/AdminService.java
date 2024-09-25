package Project.SWP391_2024_Fall_SE1866_Group1.Service;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Branch;
import Project.SWP391_2024_Fall_SE1866_Group1.Repository.*;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ClinicBranchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private RoleRepository roleRepository;

    //Add Branch to database
    public Branch createBranch(ClinicBranchRequest request) {
        Branch newBranch = new Branch();

        newBranch.setBranch_address(request.getBranch_address());
        newBranch.setBranch_description(request.getBranch_description());
        newBranch.setBranch_img(request.getBranch_img());
        newBranch.setBranch_phone(request.getBranch_phone());
        newBranch.setBranchName(request.getBranchName());

        return  branchRepository.save(newBranch);
    }

    //Get Branch
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    //Get Branch by Id
    public Branch getBranchById(int id) {
        return branchRepository.findById(id).orElseThrow(() -> new RuntimeException("Invalid Branch ID"));
    }

    //Update Branch
    public Branch updateBranch(int id, ClinicBranchRequest updateBranchRequest) {
        Branch branch = getBranchById(id);
        branch.setBranch_address(updateBranchRequest.getBranch_address());
        branch.setBranch_description(updateBranchRequest.getBranch_description());
        branch.setBranch_img(updateBranchRequest.getBranch_img());
        branch.setBranch_phone(updateBranchRequest.getBranch_phone());
        branch.setBranchName(updateBranchRequest.getBranchName());
        return branchRepository.save(branch);
    }

    //Delete Branch
    public void deleteBranch(int id) {
        branchRepository.deleteById(id);
    }
}
