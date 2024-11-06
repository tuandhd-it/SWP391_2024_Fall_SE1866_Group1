package project.dental_clinic_management.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class PassDataServiceDTO {
    int serviceId;
    String serviceName;
    double price;
}
