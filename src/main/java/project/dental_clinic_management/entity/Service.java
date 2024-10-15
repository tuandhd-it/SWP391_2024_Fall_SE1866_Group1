package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String detail;
    boolean isActive;


    @OneToMany(mappedBy = "service")
    private List<RecordService> recordService;

    public Service() {

    }

    public Service(int serviceId, String serviceName, String img, double price, String detail, boolean isActive) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.img = img;
        this.price = price;
        this.detail = detail;
        this.isActive = isActive;
    }
}
