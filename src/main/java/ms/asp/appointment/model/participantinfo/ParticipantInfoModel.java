package ms.asp.appointment.model.participantinfo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.model.BaseModel;
import ms.asp.appointment.model.ContactModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParticipantInfoModel extends BaseModel {

    private String name;
    private Participant participant;
    private ContactModel contact;
}
