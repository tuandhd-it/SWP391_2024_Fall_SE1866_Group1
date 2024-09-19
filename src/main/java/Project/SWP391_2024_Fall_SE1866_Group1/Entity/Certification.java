package Project.SWP391_2024_Fall_SE1866_Group1.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Certification {


    String certification_name;
    String certification_img;

    @Id
    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Doctor doctor;

    public Certification() {
    }

    public Certification(String certification_name, String certification_img) {
        this.certification_name = certification_name;
        this.certification_img = certification_img;
    }
}
