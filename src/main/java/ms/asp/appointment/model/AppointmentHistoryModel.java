package ms.asp.appointment.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.AppointmentType;
import ms.asp.appointment.domain.CancellationReason;
import ms.asp.appointment.domain.Reason;
import ms.asp.appointment.domain.ServiceCategory;
import ms.asp.appointment.domain.Speciality;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentHistoryModel extends BaseModel {

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
    private Reason reasonCode;
    private Long reasonId;

    private String serviceProviderJSON;
    private String participantsJSON;
    private String notesJSON;

    private long version;
    private LocalDateTime created;
    private LocalDateTime modified;
}
