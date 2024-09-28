package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int emp_id;
    String first_name;
    String last_name;
    String email;
    String phone;
    String password;
    LocalDate dob;
    String gender;
    boolean is_active;
    String address;
    double salary;
    String img;
    String description;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;



    @ManyToOne
    @JoinColumn(name = "bran_id")
    private Branch branch;

    @OneToOne(mappedBy = "employee")
    private ForgotPassword forgotPassword;

    @OneToOne
//    @JoinColumn(name = "managedBranchId", referencedColumnName = "bran_id", insertable = false, updatable = false)
    private Employee branchManaged;

    public Employee() {
    }

    public Employee(String first_name, String last_name, String email, String phone, String password, LocalDate dob, String gender, boolean is_active, String address, double salary) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.dob = dob;
        this.gender = gender;
        this.is_active = is_active;
        this.address = address;
        this.salary = salary;
    }
}
