package project.dental_clinic_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.dental_clinic_management.entity.Service;
import project.dental_clinic_management.repository.ServiceRepository;

@RestController
@RequestMapping("/service")
public class ServiceController {

    private final ServiceRepository serviceRepository;

    public ServiceController(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @PostMapping("/ban")
    public ResponseEntity<String> banService(@RequestParam int id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
        service.setActive(false);  // Deactivate the service
        serviceRepository.save(service);
        return ResponseEntity.ok("Service deactivated successfully");
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateService(@RequestParam int id) {
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found"));
        service.setActive(true);  // Activate the service
        serviceRepository.save(service);
        return ResponseEntity.ok("Service activated successfully");
    }
}

