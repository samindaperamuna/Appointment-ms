package ms.asp.appointment.domain;

import java.util.Set;

import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table("APPOINTMENT")
@EqualsAndHashCode(callSuper = true)
public class Appointment extends AuditedEntity {

    private String status;
    private CancellationReason cancellationReason;
    private int priority;
    private String description;
    private String supportingInformation;
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
    private Set<Note> notes;

    @Transient
    private Set<Participant> participants;

    @Transient
    private Period period;
    private Long periodId;

    @Version
    private long version;
}
