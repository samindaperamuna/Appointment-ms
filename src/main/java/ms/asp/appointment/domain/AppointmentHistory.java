package ms.asp.appointment.domain;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("APPOINTMENT_HISTORY")
@EqualsAndHashCode(callSuper = true)
public class AppointmentHistory extends BaseEntity {

    private Long appointmentId;
    private String status;
    private CancellationReason cancellationReason;
    private int priority;
    private String description;
    private String supportingInformation;
    private LocalDateTime start;
    private LocalDateTime end;
    private int minutesDuration;
    private String comment;
    private String patientInstruction;

    @Transient
    private ServiceProvider serviceProvider;
    private Long serviceProviderId;

    private ServiceCategory serviceCategory;
    private Speciality speciality;
    private AppointmentType appointmentType;

    @Transient
    private Reason reasonCode;
    private Long reasonId;

    @Transient
    private Set<Appointment> replaces;

    @Transient
    private Set<Slot> slots;

    @Transient
    private Set<Note> notes;

    @Transient
    private Set<Participant> participants;

    @Transient
    private Period period;
    private Long periodId;

    // Not audited
    private long version;
    private LocalDateTime created;
    private LocalDateTime modified;
}
