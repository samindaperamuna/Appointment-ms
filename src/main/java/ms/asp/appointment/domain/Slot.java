package ms.asp.appointment.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("SLOT")
@EqualsAndHashCode(callSuper = true)
public class Slot extends BaseEntity {

    private String status;
    private LocalTime start;
    private LocalTime end;
    private boolean overbooked;
    private String comment;

    @Transient
    private List<DayOfWeek> validDays;
    private String validDaysJSON;

    @Transient
    private List<Availability> availability;

    private boolean wholeWeek;
}