package ms.asp.appointment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseSlotModel extends BaseModel {

    private String status;
    private boolean overbooked;
    private String comment;
}
