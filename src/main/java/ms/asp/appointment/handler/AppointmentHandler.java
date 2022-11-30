package ms.asp.appointment.handler;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import lombok.RequiredArgsConstructor;
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
		.flatMap(pageable -> ServerResponse.ok().body(appointmentService.findAll(pageable),
			AppointmentModel.class));
    }

    public Mono<ServerResponse> byPublicId(ServerRequest req) {
	return ServerResponse.ok().body(appointmentService.findOne(req.pathVariable(ID_TEMPLATE)),
		AppointmentModel.class);
    }

    public Mono<ServerResponse> create(ServerRequest req) {
	return req.bodyToMono(AppointmentModel.class)
		.flatMap(appointment -> ServerResponse.ok().body(appointmentService.create(appointment),
			AppointmentModel.class));
    }

    public Mono<ServerResponse> update(ServerRequest req) {
	return req.bodyToMono(AppointmentModel.class)
		.flatMap(appointment -> ServerResponse.ok().body(appointmentService.update(appointment),
			AppointmentModel.class));
    }

    public Mono<ServerResponse> delete(ServerRequest req) {
	return ServerResponse.ok().body(appointmentService.delete(req.pathVariable(ID_TEMPLATE)),
		AppointmentModel.class);
    }

    public Mono<ServerResponse> reschedule(ServerRequest req) {
	var publicId = req.pathVariable(ID_TEMPLATE);

	return req.bodyToMono(PeriodModel.class)
		.flatMap(pm -> ServerResponse.ok().body(appointmentService.reschedule(publicId, pm),
			AppointmentModel.class));
    }

    public Mono<ServerResponse> history(ServerRequest req) {
	return ServerResponse.ok().body(appointmentService.history(req.pathVariable(ID_TEMPLATE)),
		AppointmentModel.class);
    }
}
