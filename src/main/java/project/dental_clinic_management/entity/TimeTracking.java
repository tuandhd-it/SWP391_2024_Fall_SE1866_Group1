package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TimeTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int timeTrackingId;
    LocalDateTime checkIn;
    LocalDateTime checkOut;

    @ManyToOne
    @JoinColumn(name="emp_id")
    public Employee employee;

    public TimeTracking(LocalDateTime checkIn, LocalDateTime checkOut, Employee employee) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.employee = employee;
    }
    public String getFormattedCheckIn() {
        if (checkIn != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return checkIn.format(formatter);
        }
        return "";
    }
    public String getFormattedCheckOut() {
        if (checkOut != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return checkOut.format(formatter);
        }
        return "";
    }
}
