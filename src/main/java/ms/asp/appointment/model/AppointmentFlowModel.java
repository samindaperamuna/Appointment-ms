package ms.asp.appointment.model;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ms.asp.appointment.domain.AppointmentType;
import ms.asp.appointment.domain.ServiceType;

@Data
@EqualsAndHashCode(callSuper = true)
public class AppointmentFlowModel extends BaseModel {

    private String description;
    private AppointmentType appointmentType;
    private Set<ServiceType> serviceType;
}
