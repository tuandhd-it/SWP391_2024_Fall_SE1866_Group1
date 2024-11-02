package project.dental_clinic_management.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import project.dental_clinic_management.exceptionHandler.FileStorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    public String saveImage(MultipartFile file) {
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path filePath = Paths.get(uploadDir, fileName);

            Files.createDirectories(filePath.getParent());

            Files.write(filePath, file.getBytes());
            System.out.println(uploadDir + "/" + fileName);
            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
        }
    }
}