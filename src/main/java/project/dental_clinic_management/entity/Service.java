package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor

public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int serviceId;
    String serviceName;
    String img;
    double price;
    @Column(columnDefinition = "TEXT")
    String detail;
    boolean isActive;
    String guarantee;
    String material;
    Integer quantity;
    String unit;

    @OneToMany(mappedBy = "service")
    private List<RecordService> recordService;

    public Service() {

    }

    public Service(int serviceId, String serviceName, String detail, String img, double price, boolean isActive, String material, String guarantee, Integer quantity, String unit) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.detail = detail;
        this.img = img;
        this.price = price;
        this.isActive = isActive;
        this.material = material;
        this.guarantee = guarantee;
        this.quantity = quantity;
        this.unit = unit;
    }

    public FileSystemResource getImgSrc() {
        String uploadDir = "/home/username/uploads";
        File file = new File(uploadDir, img);
        return new FileSystemResource(file);
    }
}
