package ms.asp.appointment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParticipantInfoModel extends BaseModel {

    private String name;
    private ContactModel contact;
}
