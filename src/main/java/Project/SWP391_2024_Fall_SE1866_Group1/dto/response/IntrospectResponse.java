package Project.SWP391_2024_Fall_SE1866_Group1.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IntrospectResponse {
    boolean valid;
}
