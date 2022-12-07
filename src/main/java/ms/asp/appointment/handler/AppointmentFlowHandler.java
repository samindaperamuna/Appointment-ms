package ms.asp.appointment.handler;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.exception.AppointmentFlowException;
import ms.asp.appointment.model.AppointmentFlowModel;
import ms.asp.appointment.service.AppointmentFlowService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AppointmentFlowHandler {

    private static final String idTemplate = "publicId";

    private final AppointmentFlowService appointmentFlowService;

    public Mono<ServerResponse> all(ServerRequest req) {
	return req.bodyToMono(Pageable.class)
		.flatMap(pageable -> ServerResponse.ok().body(appointmentFlowService.findAll(pageable),
			AppointmentFlowModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentFlowException("Couldn't fetch appointment flow {pageable}: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> byPublicId(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(appointmentFlowService.findOne(id),
		AppointmentFlowModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentFlowException("Couldn't fetch appointment flow with id: "
			    + id + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> create(ServerRequest req) {
	return req.bodyToMono(AppointmentFlowModel.class)
		.flatMap(appointment -> ServerResponse.ok().body(appointmentFlowService.create(appointment),
			AppointmentFlowModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentFlowException("Couldn't create new appointment flow: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> update(ServerRequest req) {
	return req.bodyToMono(AppointmentFlowModel.class)
		.flatMap(appointment -> ServerResponse.ok().body(appointmentFlowService.update(appointment),
			AppointmentFlowModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentFlowException("Couldn't update appointment flow: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
	var id = req.pathVariable(idTemplate);

	return ServerResponse.ok().body(appointmentFlowService.delete(id),
		AppointmentFlowModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentFlowException("Couldn't delete appointment flow with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }
}