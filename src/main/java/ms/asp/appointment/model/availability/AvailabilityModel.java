package ms.asp.appointment.model.availability;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.AvailabilityType;
import ms.asp.appointment.model.BaseModel;
import ms.asp.appointment.model.slot.SlotModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class AvailabilityModel extends BaseModel {

    private AvailabilityType availabilityType;
    private boolean available;

    private SlotModel slot;
}
