package Project.SWP391_2024_Fall_SE1866_Group1.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Specification {

    String spec_name;

    @Id
    @ManyToOne
    @JoinColumn(name = "emp_id") // Khóa ngoại liên kết với emp_id
    private Doctor doctor;

    public Specification() {
    }

    public Specification(Doctor doctor, String spec_name) {
        this.doctor = doctor;
        this.spec_name = spec_name;
    }
}
