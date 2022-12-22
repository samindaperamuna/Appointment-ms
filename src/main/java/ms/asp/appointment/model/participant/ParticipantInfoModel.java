package ms.asp.appointment.model.participant;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.model.BaseModel;
import ms.asp.appointment.model.ContactModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParticipantInfoModel extends BaseModel {

    private String name;
    private ContactModel contact;
}
