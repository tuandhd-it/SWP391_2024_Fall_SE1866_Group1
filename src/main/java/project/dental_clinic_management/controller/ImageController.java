package project.dental_clinic_management.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Value("${upload.dir}")
    private String uploadDir;

    @GetMapping("/{filename}")
    public FileSystemResource getImage(@PathVariable String filename) {
        File file = new File(uploadDir, filename);
        return new FileSystemResource(file);
    }
}
