package project.dental_clinic_management.repository;

import project.dental_clinic_management.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    public Branch findByBranchName(String branchName);
}
