
package project.dental_clinic_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;
import project.dental_clinic_management.entity.Employee;
import project.dental_clinic_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.dental_clinic_management.entity.Service;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

//    @Query("SELECT e FROM Service e WHERE e.service_name LIKE %?1%")
//    List<Service> findByName(String Keyword, Pageable pageable);

    Page<Service> findAll(Pageable pageable);

    Page<Service> findServicesByServiceNameContainingIgnoreCase(String search, Pageable pageable);

    Page<Service> findServicesByServiceId(int id, Pageable pageable);

    @Query("SELECT s FROM Service s WHERE LOWER(s.serviceName) = LOWER(:name) AND LOWER(s.material) = LOWER(:material)")
    List<Service> findServicesByServiceNameContainingIgnoreCaseAndMaterialContainingIgnoreCase(String name,String material);

    @Modifying
    @Query("UPDATE Service s SET s.img = :img WHERE s.serviceId = :oldServiceId")
    void updateImg(int oldServiceId, String img);
}
