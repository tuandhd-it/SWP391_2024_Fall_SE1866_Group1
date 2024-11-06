package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int bran_id;
    String branchName;
    String branch_description;
    String branch_address;
    String branch_phone;
    String branch_img;
    boolean active;

    @OneToMany(mappedBy = "branch")
    private List<Employee> employees;

    @OneToMany (mappedBy = "branch")
    private List<RegisterExamination> registerExaminations;

    @OneToMany(mappedBy = "branch")
    private List<Record> records;

//    @OneToMany (mappedBy = "branch")
//    private List<EquipmentImportBill> equipmentImportBills;

    @OneToOne
    @JoinColumn(name = "manager_id", referencedColumnName = "emp_id", insertable = false, updatable = false)
    private Employee manager;

    public Branch() {
    }

    public Branch(String branchName, String branch_description, String branch_address, String branch_phone, String branch_img, boolean active) {
        this.branchName = branchName;
        this.branch_description = branch_description;
        this.branch_address = branch_address;
        this.branch_phone = branch_phone;
        this.branch_img = branch_img;
        this.active = active;
    }
}
