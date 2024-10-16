package project.dental_clinic_management.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@AllArgsConstructor
public class RecordService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int service_id;
    String note;

    @ManyToOne
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    Service service;

    @ManyToOne
    @JoinColumn (name = "recordID")
    private Record record;

    public RecordService() {

    }
    public RecordService(String note, Record record) {
        this.note = note;
        this.record = record;
    }


}