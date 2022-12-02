package ms.asp.appointment.handler;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
import ms.asp.appointment.exception.AppointmentException;
import ms.asp.appointment.model.AppointmentModel;
import ms.asp.appointment.model.PeriodModel;
import ms.asp.appointment.service.AppointmentService;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AppointmentHandler {

    private static final String ID_TEMPLATE = "publicId";

    private final AppointmentService appointmentService;

    public Mono<ServerResponse> all(ServerRequest req) {
	return req.bodyToMono(Pageable.class)
		.flatMap(pageable -> ServerResponse.ok()
			.body(appointmentService.findAll(pageable), AppointmentModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't fetch appointments {pageable}: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> byPublicId(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return ServerResponse.ok()
		.body(appointmentService.findOne(id), AppointmentModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't fetch appointment with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> create(ServerRequest req) {
	return req.bodyToMono(AppointmentModel.class)
		.flatMap(appointment -> ServerResponse.ok()
			.body(appointmentService.create(appointment), AppointmentModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't create new appointment: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> update(ServerRequest req) {
	return req.bodyToMono(AppointmentModel.class)
		.flatMap(appointment -> ServerResponse.ok()
			.body(appointmentService.update(appointment), AppointmentModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't update appointment: "
			    + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return ServerResponse.ok()
		.body(appointmentService.delete(id), AppointmentModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't delete appointment with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> reschedule(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return req.bodyToMono(PeriodModel.class)
		.flatMap(pm -> ServerResponse.ok()
			.body(appointmentService.reschedule(id, pm), AppointmentModel.class))
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't reschedule appointment with id:" + id
			    + ": " + e.getLocalizedMessage()));
		});
    }

    public Mono<ServerResponse> history(ServerRequest req) {
	var id = req.pathVariable(ID_TEMPLATE);

	return ServerResponse.ok()
		.body(appointmentService.history(req.pathVariable(id)), AppointmentModel.class)
		.onErrorResume(e -> {
		    return Mono.error(new AppointmentException("Couldn't get history for appointment with id: " + id
			    + ": " + e.getLocalizedMessage()));
		});
    }
}
