package project.dental_clinic_management.repository;

import project.dental_clinic_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Role findById(int id);
    public Role findByRoleName(String roleName);
}
