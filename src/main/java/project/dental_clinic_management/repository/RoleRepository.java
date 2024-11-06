package project.dental_clinic_management.repository;

import org.springframework.data.jpa.repository.Query;
import project.dental_clinic_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("select r FROM Role r WHERE r.role_id = ?1")
    Role findById(int id);
    Role findByRoleName(String roleName);
}
