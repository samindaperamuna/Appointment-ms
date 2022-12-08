package ms.asp.appointment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ParticipantModel extends BaseModel {

    private String type;
    private boolean required;
    private String status;

    private ParticipantInfoModel participantInfo;
}
