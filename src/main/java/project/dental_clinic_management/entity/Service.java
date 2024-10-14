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

    double price;
    //String detail;
    boolean isActive;


    @OneToMany(mappedBy = "service")
    private List<RecordService> recordService;

    public Service() {

    }
    public Service(String service_name, double price, Boolean isActive, List<RecordService> recordService) {
        this.serviceName= service_name;
        this.price = price;
        this.isActive = isActive;
        this.recordService = recordService;
    }


}
