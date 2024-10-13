package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TimeTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int timeTrackingId;
    Date checkIn;
    Date checkOut;

    @ManyToOne
    @JoinColumn(name="emp_id")
    private Employee employee;

    public TimeTracking(Date checkIn, Date checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
