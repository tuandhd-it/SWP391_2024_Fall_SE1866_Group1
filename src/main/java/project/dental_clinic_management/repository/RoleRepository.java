package project.dental_clinic_management.repository;

import project.dental_clinic_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findById(int id);
    Role findByRoleName(String roleName);
}
