package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    @Column(columnDefinition = "TEXT")
    String note;
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
    public String getDayFormattedCheckIn() {
        if (checkIn != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
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

    public String getTotalWorkingTime() {
        if (checkIn != null && checkOut != null) {
            long diff = java.time.Duration.between(checkIn, checkOut).toMinutes();
            long hours = diff / 60;
            long minutes = diff % 60;
            return hours + "h" + minutes + "m";
        }
        return "";
    }
}
