package ms.asp.appointment.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.AvailabilityType;

@Data
@EqualsAndHashCode(callSuper = true)
public class AvailabilityModel extends BaseModel {

    private AvailabilityType availabilityType;
    private boolean available;
    
    private SlotModel slot;
}
