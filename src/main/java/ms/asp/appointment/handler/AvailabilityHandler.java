package ms.asp.appointment.handler;

import org.springframework.stereotype.Component;

import ms.asp.appointment.domain.Availability;
import ms.asp.appointment.model.availability.AvailabilityModel;
import ms.asp.appointment.service.AvailabilityService;

@Component
public class AvailabilityHandler extends AbstractHandler<Availability, Long, AvailabilityModel, AvailabilityService> {

    public AvailabilityHandler(AvailabilityService service) {
	super(service, new AvailabilityModel());
    }
}
