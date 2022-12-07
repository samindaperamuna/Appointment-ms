package ms.asp.appointment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParticipantModel extends BaseModel {
    private String name;
    private ContactModel contact;
    private String type;
}
