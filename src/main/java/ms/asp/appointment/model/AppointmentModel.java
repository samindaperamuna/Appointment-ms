package ms.asp.appointment.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.AppointmentType;
import ms.asp.appointment.domain.CancellationReason;
import ms.asp.appointment.domain.Reason;
import ms.asp.appointment.domain.ServiceCategory;
import ms.asp.appointment.domain.Speciality;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentModel extends BaseModel {
    private String status;
    private CancellationReason cancellationReason;
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

    @JsonIgnore
    private List<AppointmentModel> replaces;
    private List<NoteModel> notes;
    private List<ParticipantModel> participants;

    private PeriodModel requestedPeriod;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime created;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime modified;
}
