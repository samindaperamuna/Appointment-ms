package ms.asp.appointment.handler;

import org.springframework.stereotype.Component;

import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.model.ParticipantModel;
import ms.asp.appointment.service.ParticipantService;

@Component
public class ParticipantInfoHandler extends AbstractHandler<Participant, Long, ParticipantModel, ParticipantService> {

    public ParticipantInfoHandler(ParticipantService service) {
	super(service, new ParticipantModel());
    }
}
