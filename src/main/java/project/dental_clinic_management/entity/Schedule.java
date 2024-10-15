package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int scheduleId;
    LocalDate date;
    boolean shift;

    @ManyToOne
    @JoinColumn(name = "emp_id")
    private Employee employee;

    public Schedule(LocalDate date, boolean shift) {
        this.date = date;
        this.shift = shift;
    }
}
