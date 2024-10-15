package project.dental_clinic_management.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCreateRequest {
    int serviceId;
    String serviceName;
    double price;
    String detail;
    boolean isActive;
}
