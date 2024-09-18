package Project.SWP391_2024_Fall_SE1866_Group1.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {

    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dob;
}
