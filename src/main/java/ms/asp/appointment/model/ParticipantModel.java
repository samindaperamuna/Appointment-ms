package ms.asp.appointment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.Contact;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParticipantModel extends BaseModel {
    private String name;
    private Contact contact;
    private String type;
}
