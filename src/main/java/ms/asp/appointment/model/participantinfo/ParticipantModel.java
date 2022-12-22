package ms.asp.appointment.model.participantinfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.model.BaseModel;
import ms.asp.appointment.model.appointment.AppointmentModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParticipantModel extends BaseModel {

    private String type;
    private boolean required;
    private String status;

    private AppointmentModel appointment;
}
