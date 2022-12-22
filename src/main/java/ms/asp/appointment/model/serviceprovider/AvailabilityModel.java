package ms.asp.appointment.model.serviceprovider;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.AvailabilityType;
import ms.asp.appointment.model.BaseModel;

@Data
@EqualsAndHashCode(callSuper = true)
public class AvailabilityModel extends BaseModel {

    private AvailabilityType availabilityType;
    private boolean available;
}
