package ms.asp.appointment.handler;

import org.springframework.stereotype.Component;

import ms.asp.appointment.domain.Participant;
import ms.asp.appointment.model.participant.ParticipantModel;
import ms.asp.appointment.service.ParticipantService;

@Component
public class ParticipantHandler extends AbstractHandler<Participant, Long, ParticipantModel, ParticipantService> {

    public ParticipantHandler(ParticipantService service) {
	super(service, new ParticipantModel());
    }
}
