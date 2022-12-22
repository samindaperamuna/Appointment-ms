package ms.asp.appointment.model.participant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.AppointmentType;
import ms.asp.appointment.domain.CancellationReason;
import ms.asp.appointment.domain.ServiceCategory;
import ms.asp.appointment.domain.Speciality;
import ms.asp.appointment.model.BaseModel;
import ms.asp.appointment.model.PeriodModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentModel extends BaseModel {
    private String status;
    private CancellationReason cancellationReason;
    private ServiceCategory serviceCategory;
    private Speciality speciality;
    private AppointmentType appointmentType;
    private int priority;
    private String description;
    private String supportingInformation;
    private int minutesDuration;
    private String comment;
    private String patientInstruction;

    private PeriodModel requestedPeriod;
}
