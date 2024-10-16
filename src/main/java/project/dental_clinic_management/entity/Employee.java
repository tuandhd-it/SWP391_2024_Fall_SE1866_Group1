package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
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

    @OneToMany (mappedBy = "employee")
    private List<TimeTracking> timeTrackings;

    @OneToMany (mappedBy = "employee")
    private List<RegisterExamination> registerExaminations;

    @OneToMany (mappedBy = "employee")
    private List<Record> records;

    @OneToMany (mappedBy = "employee")
    private List<EquipmentImportBill> equipmentImportBills;

    @OneToMany (mappedBy = "employee")
    private List<Invoice> invoices;

    @OneToMany (mappedBy = "employee")
    private List<Schedule> schedules;

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

    // Method format LocalDate thành format dd/mm/yyyy
    public static String formatLocalDate(LocalDate date) {
        if (date == null) {
            return ""; // trả về "" nếu date null
        }
        // Tạo chuỗi bằng pattern
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return date.format(formatter);
    }

}
