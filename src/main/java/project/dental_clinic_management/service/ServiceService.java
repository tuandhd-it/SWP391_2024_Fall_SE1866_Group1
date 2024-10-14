package project.dental_clinic_management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import project.dental_clinic_management.entity.Service;
import project.dental_clinic_management.repository.EmployeeRepository;
import project.dental_clinic_management.repository.ServiceRepository;

import java.util.List;

@org.springframework.stereotype.Service
public class ServiceService {
    @Autowired
    private ServiceRepository serviceRepository;

    public Page<Service> getAllByPage(int page,String sort, String search,String typeOfSearch) {
        Sort sortOption = switch (sort) {
            case "priceAsc" -> Sort.by("price").ascending();
            case "priceDesc" -> Sort.by("price").descending();
            case "nameDesc" -> Sort.by("serviceName").descending();
            case "nameAsc" -> Sort.by("serviceName").ascending();
            default -> Sort.by("serviceId").ascending();
        };
        Pageable pageable = PageRequest.of(page,6,sortOption);
        if (typeOfSearch.equals("name")) return serviceRepository.findServicesByServiceNameContainingIgnoreCase(search,pageable);
        else{
            int id = -1;
            try{
                id = Integer.parseInt(search);
            }catch(NumberFormatException e){}
            return serviceRepository.findServicesByServiceId(id,pageable);
        }

    }
    public long countAll() {
        return serviceRepository.count();
    }
}
