package ms.asp.appointment.model;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.AppointmentType;
import ms.asp.appointment.domain.Reason;
import ms.asp.appointment.domain.ServiceCategory;
import ms.asp.appointment.domain.ServiceType;
import ms.asp.appointment.domain.Slot;
import ms.asp.appointment.domain.Speciality;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentModel extends BaseModel {
    private String status;
    private String cancellationReason;
    private ServiceCategory serviceCategory;
    private ServiceType serviceType;
    private Speciality specialty;
    private AppointmentType appointmentType;
    private Reason reasonCode;
    private int priority;
    private String description;
    private Set<AppointmentModel> replaces;
    private String supportingInformation;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime start;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm", iso = ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime end;
    private int minutesDuration;
    private Set<Slot> slots;
    private LocalDateTime created;
    private LocalDateTime modified;
    private String comment;
    private String patientInstruction;
    private Set<NoteModel> notes;
    private Set<ParticipantModel> participants;
    private PeriodModel requestedPeriod;
}
