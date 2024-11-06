package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
    String specification;
    String certification;

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
    private Branch branchManaged;

    @OneToMany (mappedBy = "employee")
    private List<TimeTracking> timeTrackings;

    @OneToMany (mappedBy = "employee")
    private List<RegisterExamination> registerExaminations;

    @OneToMany (mappedBy = "employee")
    private List<Record> records;

//    @OneToMany (mappedBy = "employee")
//    private List<EquipmentImportBill> equipmentImportBills;

    @OneToMany (mappedBy = "employee")
    private List<Invoice> invoices;

    @OneToMany (mappedBy = "employee")
    private List<Schedule> schedules;

    public Employee() {
    }

    public Employee(String first_name, String last_name, String email, String phone, String password, LocalDate dob, String gender, boolean is_active, String address, double salary, String specification, String certification, Role role) {
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
        this.specification = specification;
        this.certification = certification;
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

    public String getFormattedDob() {
        return dob != null ? dob.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "";
    }

}
