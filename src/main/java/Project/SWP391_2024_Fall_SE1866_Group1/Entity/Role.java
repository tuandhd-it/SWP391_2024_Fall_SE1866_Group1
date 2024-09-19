package Project.SWP391_2024_Fall_SE1866_Group1.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int role_id;
    String role_name;

    @OneToMany(mappedBy = "role")
    private Set<Employee> employees;
    public Role() {
    }

    public Role(String role_name) {
        this.role_name = role_name;
    }
}