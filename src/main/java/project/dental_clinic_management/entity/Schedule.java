package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@AllArgsConstructor
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
