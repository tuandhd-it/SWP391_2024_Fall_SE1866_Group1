package Project.SWP391_2024_Fall_SE1866_Group1.Controller;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Branch;
import Project.SWP391_2024_Fall_SE1866_Group1.Service.AdminService;
import Project.SWP391_2024_Fall_SE1866_Group1.dto.request.ClinicBranchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/branch")
    Branch createBranch(@RequestBody ClinicBranchRequest branchRequest) {
        return adminService.createBranch(branchRequest);
    }

    @GetMapping
    List<Branch> getAllBranches() {
        return adminService.getAllBranches();
    }



}
