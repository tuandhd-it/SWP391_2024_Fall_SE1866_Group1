package Project.SWP391_2024_Fall_SE1866_Group1.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int bran_id;
    String branchName;
    String branch_description;
    String branch_address;
    String branch_phone;

    @OneToMany(mappedBy = "branch")
    private List<Employee> employees;

    public Branch() {
    }

    public Branch(String branch_name, String branch_description, String branch_address, String branch_phone) {
        this.branchName = branch_name;
        this.branch_description = branch_description;
        this.branch_address = branch_address;
        this.branch_phone = branch_phone;
    }
}
