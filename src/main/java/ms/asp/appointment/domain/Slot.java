package ms.asp.appointment.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("SLOT")
@EqualsAndHashCode(callSuper = true)
public class Slot extends BaseEntity {

    private String status;
    private LocalDateTime start;
    private LocalDateTime end;
    private boolean overbooked;
    private String comment;

    @Transient
    private Schedule schedule;
    private Long scheduleId;
}