package ms.asp.appointment.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.AppointmentType;
import ms.asp.appointment.domain.Reason;
import ms.asp.appointment.domain.ServiceCategory;
import ms.asp.appointment.domain.Speciality;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentModel extends BaseModel {
    private String status;
    private String cancellationReason;
    private AppointmentServiceProviderModel serviceProvider;
    private ServiceCategory serviceCategory;
    private Speciality speciality;
    private AppointmentType appointmentType;
    private Reason reasonCode;
    private int priority;
    private String description;
    private String supportingInformation;
    private int minutesDuration;
    private String comment;
    private String patientInstruction;

    private List<AppointmentModel> replaces;
    private List<NoteModel> notes;
    private List<ParticipantModel> participants;
    
    private PeriodModel requestedPeriod;
    
    private LocalDateTime created;
    private LocalDateTime modified;
}
