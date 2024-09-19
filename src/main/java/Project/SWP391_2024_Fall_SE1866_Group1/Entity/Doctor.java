package Project.SWP391_2024_Fall_SE1866_Group1.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Doctor {

    @Id
    int doctor_id; // Khóa ngoại từ bảng Employee
    String doctor_img;
    String doctor_description;

    @OneToOne
    @MapsId // Sử dụng để ánh xạ doctor_id với emp_id
    @JoinColumn(name = "doctor_id", referencedColumnName = "emp_id")
    private Employee employee;

    @OneToMany(mappedBy = "doctor")
    private Set<Certification> certification;

    @OneToMany(mappedBy = "doctor")
    private Set<Specification> specification;

    public Doctor() {
    }

    public Doctor(String doctor_img, String doctor_description) {
        this.doctor_img = doctor_img;
        this.doctor_description = doctor_description;
    }
}
