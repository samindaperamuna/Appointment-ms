package ms.asp.appointment.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.exception.ServiceProviderException;
import ms.asp.appointment.exception.SlotException;
import ms.asp.appointment.model.SlotModel;
import ms.asp.appointment.service.SlotService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SlotHandler {

    private static final String idTemplate = "publicId";

    private final SlotService slotService;

    public Mono<ServerResponse> byPublicId(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(slotService.findOne(id),
		SlotModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new ServiceProviderException("Couldn't fetch slot with id: "
			    + id + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> create(ServerRequest req) {
	return req.bodyToMono(SlotModel.class)
		.flatMap(slot -> ServerResponse.ok().body(slotService.create(slot), SlotModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new ServiceProviderException("Couldn't create new slot: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> update(ServerRequest req) {
	return req.bodyToMono(SlotModel.class)
		.flatMap(slot -> ServerResponse.ok().body(slotService.update(slot), SlotModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new SlotException("Couldn't update slot: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(slotService.delete(id), SlotModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new SlotException("Couldn't delete slot with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }
}
