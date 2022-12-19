package ms.asp.appointment.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.exception.ServiceProviderException;
import ms.asp.appointment.exception.SlotException;
import ms.asp.appointment.model.AvailabilityModel;
import ms.asp.appointment.service.AvailabilityService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AvailabilityHandler {

    private static final String idTemplate = "publicId";

    private final AvailabilityService availabilityService;

    public Mono<ServerResponse> byPublicId(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(availabilityService.findOne(id),
		AvailabilityModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new ServiceProviderException("Couldn't fetch availability with id: "
			    + id + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> create(ServerRequest req) {
	return req.bodyToMono(AvailabilityModel.class)
		.flatMap(availability -> ServerResponse.ok().body(availabilityService.create(availability),
			AvailabilityModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new ServiceProviderException("Couldn't create new availability: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> update(ServerRequest req) {
	return req.bodyToMono(AvailabilityModel.class)
		.flatMap(availability -> ServerResponse.ok().body(availabilityService.update(availability),
			AvailabilityModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new SlotException("Couldn't update availability: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(availabilityService.delete(id), AvailabilityModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new SlotException("Couldn't delete availability with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }
}
