package ms.asp.appointment.handler;

import org.springframework.stereotype.Component;

import ms.asp.appointment.domain.ParticipantInfo;
import ms.asp.appointment.model.participantinfo.ParticipantInfoModel;
import ms.asp.appointment.service.ParticipantInfoService;

@Component
public class ParticipantInfoHandler
	extends AbstractHandler<ParticipantInfo, Long, ParticipantInfoModel, ParticipantInfoService> {

    public ParticipantInfoHandler(ParticipantInfoService service) {
	super(service, new ParticipantInfoModel());
    }
}
