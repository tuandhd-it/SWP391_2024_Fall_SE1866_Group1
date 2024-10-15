package project.dental_clinic_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.dental_clinic_management.dto.request.ServiceCreateRequest;
import project.dental_clinic_management.entity.Service;
import project.dental_clinic_management.repository.ServiceRepository;
import project.dental_clinic_management.service.ServiceService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@RestController
@RequestMapping("/service")
public class ServiceController {

    private final ServiceRepository serviceRepository;
    private final ServiceService serviceService;

    public ServiceController(ServiceRepository serviceRepository, ServiceService serviceService) {
        this.serviceRepository = serviceRepository;
        this.serviceService = serviceService;
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
    @PostMapping("/addService")
    public ResponseEntity<String> addService(@RequestParam("serviceName") String serviceName,
                                             @RequestParam("price") double price,
                                             @RequestParam("detail") String detail,
                                             @RequestParam("isActive") boolean isActive,
                                             @RequestParam("img") MultipartFile imgFile,
                                             Model model) {
        // Handle the image upload
        String linkImg="";
        String folder = "src/main/resources/static/img/";
        File directory = new File(folder);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        if (!imgFile.isEmpty()) {
            try {
                String originalFilename = imgFile.getOriginalFilename();
                String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

                // Generate a unique filename using UUID
                String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

                // Define the complete file path
                Path path = Paths.get(folder + uniqueFilename);

                // Save the file to the specified directory
                Files.write(path, imgFile.getBytes());
                linkImg = uniqueFilename;
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "Failed to upload image.");
                return ResponseEntity.ofNullable("");
            }
        }
        Service service = new Service();
        service.setServiceName(serviceName);
        service.setPrice(price);
        service.setDetail(detail);
        service.setImg(linkImg);
        service.setActive(isActive);
        serviceService.saveService(service);

        return ResponseEntity.ok("Service add successfully");

    }
}

