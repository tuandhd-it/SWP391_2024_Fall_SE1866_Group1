package Project.SWP391_2024_Fall_SE1866_Group1.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account {

    @Id
    int acc_id;
    String username;
    String password;
    String isActivated;

    @OneToOne(mappedBy = "account")
    private Employee employee;

    public Account() {
    }

    public Account(String username, String password, String isActivated) {
        this.username = username;
        this.password = password;
        this.isActivated = isActivated;
    }
}
