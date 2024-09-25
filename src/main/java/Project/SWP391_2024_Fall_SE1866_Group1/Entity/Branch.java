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
    String branch_img;

    @OneToMany(mappedBy = "branch")
    private List<Employee> employees;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "emp_id", insertable = false, updatable = false)
    private Employee manager;

    public Branch() {
    }

    public Branch(String branchName, String branch_description, String branch_address, String branch_phone, String branch_img) {
        this.branchName = branchName;
        this.branch_description = branch_description;
        this.branch_address = branch_address;
        this.branch_phone = branch_phone;
        this.branch_img = branch_img;
    }
}
