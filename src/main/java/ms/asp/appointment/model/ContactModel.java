package ms.asp.appointment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ContactModel extends BaseModel {
    private String location;
    private String telephone;
    private String email;
}
