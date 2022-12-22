package ms.asp.appointment.model.participant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.model.BaseModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParticipantModel extends BaseModel {

    private String type;
    private boolean required;
    private String status;

    private AppointmentModel appointment;
    private ParticipantInfoModel participantInfo;
}
