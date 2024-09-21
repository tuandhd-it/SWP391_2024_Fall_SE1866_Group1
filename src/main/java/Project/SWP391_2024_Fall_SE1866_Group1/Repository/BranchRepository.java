package Project.SWP391_2024_Fall_SE1866_Group1.Repository;

import Project.SWP391_2024_Fall_SE1866_Group1.Entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Integer> {
    public Branch findByName(String name);
}
