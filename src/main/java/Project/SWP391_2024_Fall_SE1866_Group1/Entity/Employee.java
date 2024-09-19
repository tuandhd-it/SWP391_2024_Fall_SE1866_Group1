package Project.SWP391_2024_Fall_SE1866_Group1.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

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
    String address;
    String status;
    double salary;

    @OneToOne(mappedBy = "employee")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "bran_id")
    private Branch branch;

    @OneToOne
    private Account account;

    public Employee() {
    }

    public Employee(String first_name, String last_name, String email, String phone, String address, String status, double salary) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.status = status;
        this.salary = salary;
    }
}
