package ms.asp.appointment.domain;

import java.time.LocalDateTime;

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
    private int minutesDuration;
    private LocalDateTime start;
    private LocalDateTime end;
    private String comment;
    private String patientInstruction;

    private ServiceCategory serviceCategory;
    private Speciality speciality;
    private AppointmentType appointmentType;

    @Transient
    private Reason reasonCode;
    private Long reasonId;

    // Flattened sets
    private String serviceProviderJSON;
    private String participantsJSON;
    private String notesJSON;

    // Not audited
    private long version;
    private LocalDateTime created;
    private LocalDateTime modified;
}
