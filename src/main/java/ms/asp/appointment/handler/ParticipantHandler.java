package ms.asp.appointment.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.exception.ServiceProviderException;
import ms.asp.appointment.exception.SlotException;
import ms.asp.appointment.model.ParticipantModel;
import ms.asp.appointment.service.ParticipantService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ParticipantHandler {

    private static final String idTemplate = "publicId";

    private final ParticipantService participantService;

    public Mono<ServerResponse> byPublicId(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(participantService.findOne(id),
		ParticipantModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new ServiceProviderException("Couldn't fetch participant with id: "
			    + id + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> create(ServerRequest req) {
	return req.bodyToMono(ParticipantModel.class)
		.flatMap(participant -> ServerResponse.ok().body(participantService.create(participant),
			ParticipantModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new ServiceProviderException("Couldn't create new participant: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> update(ServerRequest req) {
	return req.bodyToMono(ParticipantModel.class)
		.flatMap(participant -> ServerResponse.ok().body(participantService.update(participant),
			ParticipantModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new SlotException("Couldn't update participant: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(participantService.delete(id), ParticipantModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new SlotException("Couldn't delete participant with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }
}
