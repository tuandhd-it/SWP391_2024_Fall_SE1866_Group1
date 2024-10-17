package project.dental_clinic_management.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class WaitingRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int waitingRoomID;
    boolean isAvailable;
    int capacity;

    @OneToOne
    @JoinColumn (name = "branch_id")
    Branch branch;

    public WaitingRoom(boolean isAvailable, int capacity) {
        this.isAvailable = isAvailable;
        this.capacity = capacity;
    }
}
