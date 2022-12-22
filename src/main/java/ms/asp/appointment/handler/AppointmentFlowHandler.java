package ms.asp.appointment.handler;

import org.springframework.stereotype.Component;

import ms.asp.appointment.domain.AppointmentFlow;
import ms.asp.appointment.model.AppointmentFlowModel;
import ms.asp.appointment.service.AppointmentFlowService;

@Component
public class AppointmentFlowHandler
	extends AbstractHandler<AppointmentFlow, Long, AppointmentFlowModel, AppointmentFlowService> {

    public AppointmentFlowHandler(AppointmentFlowService service) {
	super(service, new AppointmentFlowModel());
    }
}